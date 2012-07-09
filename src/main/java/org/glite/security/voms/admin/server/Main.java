package org.glite.security.voms.admin.server;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.italiangrid.utils.https.JettyAdminService;
import org.italiangrid.utils.https.JettyRunThread;
import org.italiangrid.utils.https.JettyShutdownTask;
import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.ServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static final String DEFAULT_WAR = "/usr/share/webapps/glite-security-voms-admin.war";
	
	public static final String DEFAULT_HOST = "localhost";
	public static final String DEFAULT_PORT = "15000";
	public static final String DEFAULT_SHUTDOWN_PORT="15001";
	
	public static final String DEFAULT_CERT = "/etc/grid-security/hostcert.pem";
	public static final String DEFAULT_KEY = "/etc/grid-security/hostkey.pem";
	public static final String DEFAULT_TRUSTSTORE_DIR = "/etc/grid-security/certificates";
	
	private static final String ARG_VO = "vo";
	private static final String ARG_WAR = "war";
	private static final String ARG_CONFDIR = "confdir";
	
	private static final String ARG_HOST = "host";
	private static final String ARG_PORT = "port";
	private static final String ARG_SHUTDOWN_PORT = "shutdown_port";
	
	private static final String ARG_CERT = "cert";
	private static final String ARG_KEY = "key";
	private static final String ARG_TRUSTDIR = "trustdir";
	
	
	private Options cliOptions;
	private CommandLineParser parser = new GnuParser();
	
	private String vo;
	private String war;
	
	private String host;
	private String port;
	private String shutdownPort;
	
	private String certFile;
	private String keyFile;
	private String trustDir;
	
	private Server server;
	private JettyAdminService shutdownService;
	
	private WebAppContext vomsWebappContext;
	
	private void initOptions(){
		cliOptions = new Options();
		
		Option voOption = new Option(ARG_VO, true,"The VO this server will run for.");
		voOption.setRequired(true);
		
		cliOptions.addOption(voOption);
		
		Option warOption = new Option(ARG_WAR, true, "The WAR used to start this server.");
		
		cliOptions.addOption(warOption);
		
		Option hostOption = new Option(ARG_HOST, 
				true, 
				"the host this server will bind to.");
		
		Option portOption = new Option(ARG_PORT, 
				true, 
				"The port this server will bind to.");
		
		Option shutdownPortOption = new Option(ARG_SHUTDOWN_PORT, 
				true, 
				"The port where the shutdown service will bind to.");
		
		cliOptions.addOption(hostOption);
		cliOptions.addOption(portOption);
		cliOptions.addOption(shutdownPortOption);
		
		Option confDirOption = new Option(ARG_CONFDIR, true, "The configuration directory where VOMS configuration is stored.");
		
		cliOptions.addOption(confDirOption);
		
		Option certOption = new Option(ARG_CERT, true, "The X.509 certificate (in PEM format) used by the VOMS server.");
		Option keyOption = new Option(ARG_KEY, true, "The X.509 key (in PEM format) used by the VOMS server.");
		Option trustDir = new Option(ARG_TRUSTDIR, true, "The trust store directory");
		
		cliOptions.addOption(certOption);
		cliOptions.addOption(keyOption);
		cliOptions.addOption(trustDir);
		
	}
	
	private void failAndExit(String errorMessage, Throwable t){
		
		if (t != null){
			log.error(errorMessage+": {}", t.getMessage());
		}else{
			
			log.error(errorMessage);
			
		}
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Main", cliOptions);
		
		System.exit(1);
		
	}
	private void parseCommandLineOptions(String[] args){
		
		try {
			
			CommandLine cmdLine = parser.parse(cliOptions, args);
			
			vo = cmdLine.getOptionValue(ARG_VO);
			war = cmdLine.getOptionValue(ARG_WAR, DEFAULT_WAR);
			host = cmdLine.getOptionValue(ARG_HOST, DEFAULT_HOST);
			port = cmdLine.getOptionValue(ARG_PORT, DEFAULT_PORT);
			shutdownPort = cmdLine.getOptionValue(ARG_SHUTDOWN_PORT, DEFAULT_SHUTDOWN_PORT);
			
			certFile = cmdLine.getOptionValue("cert", DEFAULT_CERT);
			keyFile = cmdLine.getOptionValue("key", DEFAULT_KEY);
			trustDir = cmdLine.getOptionValue("trustDir", DEFAULT_TRUSTSTORE_DIR);
		
		} catch (ParseException e) {
			
			failAndExit("Error parsing command line arguments", e);

		}
	}
	
	
	public Main(String[] args) {
		
		initOptions();
		parseCommandLineOptions(args);
		configureJettyServer();
		configureShutdownService();
		start();
	}
	
	
	protected SSLOptions setupSSLOptions(){
		SSLOptions options = new SSLOptions();
		
		options.setCertificateFile(certFile);
		options.setKeyFile(keyFile);
		options.setTrustStoreDirectory(trustDir);
		
		return options;
		
	}
	
	protected void configureJettyServer(){
		
		server = ServerFactory.newServer(host, 
				Integer.parseInt(port), 
				setupSSLOptions());
		
		vomsWebappContext = new WebAppContext();
		vomsWebappContext.setContextPath(String.format("/voms/%s", vo));
		vomsWebappContext.setWar(war);
		vomsWebappContext.setInitParameter("VO_NAME", vo);
		vomsWebappContext.setParentLoaderPriority(true);
		
		HandlerCollection handlers = new HandlerCollection();
		
		handlers.setHandlers(new Handler[]{vomsWebappContext, new DefaultHandler()});
		server.setHandler(handlers);
		
		
	}
	
	protected void configureShutdownService(){
		
		// FIXME: Add support for secured shutdown service (i.e. which requires a password)
		shutdownService = new JettyAdminService("localhost",  Integer.parseInt(shutdownPort), null);
		shutdownService.registerShutdownTask(new JettyShutdownTask(server));
		
		
	}
	
	private void checkStatus() throws Throwable{
		
		if (vomsWebappContext.getUnavailableException() != null)
			throw vomsWebappContext.getUnavailableException();
	}
	
	private void start() {
		
		JettyRunThread vomsService =  new JettyRunThread(server);
		
		vomsService.start();
		
		try{
			
			checkStatus();
			shutdownService.start();
			
		
		} catch(Throwable t){
			
			log.error("Error starting VOMS server {}", t.getClass().getName(), t);
			System.exit(-1);
		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new Main(args);

	}

}
