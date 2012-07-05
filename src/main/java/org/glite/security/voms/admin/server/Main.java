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
import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.ServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static final String DEFAULT_WAR = "/usr/share/webapps/glite-security-voms-admin.war";
	public static final String DEFAULT_CERT = "/etc/grid-security/hostcert.pem";
	public static final String DEFAULT_KEY = "/etc/grid-security/hostkey.pem";
	public static final String DEFAULT_TRUSTSTORE_DIR = "/etc/grid-security/certificates";
	
	private static final String ARG_VO = "vo";
	private static final String ARG_WAR = "war";
	
	private static final String ARG_HOST = "host";
	private static final String ARG_PORT = "port";
	
	private static final String ARG_CERT = "cert";
	private static final String ARG_KEY = "key";
	private static final String ARG_TRUSTDIR = "trustdir";
	
	
	private Options cliOptions;
	private CommandLineParser parser = new GnuParser();
	
	private String vo;
	private String war;
	
	private String host;
	private String port;
	
	private String certFile;
	private String keyFile;
	private String trustDir;
	
	private Server server;
	
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
		
		cliOptions.addOption(hostOption);
		cliOptions.addOption(portOption);
		
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
			
			vo = cmdLine.getOptionValue("vo");
			war = cmdLine.getOptionValue("war", DEFAULT_WAR);
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
		
		WebAppContext context = new WebAppContext();
		context.setContextPath(String.format("/voms/%s", vo));
		context.setWar(war);
		context.setInitParameter("VO_NAME", vo);
		
		HandlerCollection handlers = new HandlerCollection();
		
		handlers.setHandlers(new Handler[]{context, new DefaultHandler()});
		
		server.setHandler(handlers);
		
		
	}
	
	
	private void checkStatus() throws Throwable{
		
		for (Handler h: server.getHandlers()){
			
			if (h instanceof WebAppContext){
				
				WebAppContext c = (WebAppContext)h;
				
				if (c.getUnavailableException() != null)
					throw c.getUnavailableException();
			}
		}
	}
	
	private void start() {
		
		try{
			
			server.start();
			checkStatus();
			server.join();
		
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
