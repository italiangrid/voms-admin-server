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
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
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

	private static final String ARG_WAR = "war";
	private static final String ARG_VO = "vo";
	private static final String ARG_CONFDIR = "confdir";
	
	
	private Options cliOptions;
	private CommandLineParser parser = new GnuParser();

	private String war;
	private String vo;
	private String confDir;
	
	private String host;
	private String port;
	private String shutdownPort;
	        
	private String certFile;
	private String keyFile;
	private String trustDir;
	private long trustDirRefreshIntervalInMsec;
	
	private Server server;
	private JettyAdminService shutdownService;
	
	private WebAppContext vomsWebappContext;
	
	private void initOptions(){
		
		cliOptions = new Options();

		Option warOption = new Option(ARG_WAR, true, "The WAR used to start this server.");
		
		cliOptions.addOption(warOption);
		
		Option voOption = new Option(ARG_VO, true,"The VO this server will run for.");
		voOption.setRequired(true);
		
		cliOptions.addOption(voOption);
		
		Option confDirOption = new Option(ARG_CONFDIR, true, "The configuration directory where VOMS configuration is stored.");
		
		cliOptions.addOption(confDirOption);
		
	}
	
	private void failAndExit(String errorMessage, Throwable t){
		
		if (t != null) {

			log.error(errorMessage+": {}", t.getMessage());
		
		} else {
			
			log.error(errorMessage);
			
		}
		
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Main", cliOptions);
		
		System.exit(1);
		
	}
	
	private void parseCommandLineOptions(String[] args){
		
		try {
			
			CommandLine cmdLine = parser.parse(cliOptions, args);

			war = cmdLine.getOptionValue(ARG_WAR, DEFAULT_WAR);
			vo = cmdLine.getOptionValue(ARG_VO);
			confDir = cmdLine.getOptionValue(ARG_CONFDIR);
			
		} catch (ParseException e) {
			
			failAndExit("Error parsing command line arguments", e);

		}
	}
	
	private void loadConfiguration() {
		
		System.setProperty(VOMSConfigurationConstants.VO_NAME, vo);
		
		VOMSConfiguration conf = VOMSConfiguration.load(null);
		conf.loadServiceProperties();
		
		host = conf.getString(VOMSConfigurationConstants.VOMS_SERVICE_HOSTNAME, DEFAULT_HOST);
		port = conf.getString(VOMSConfigurationConstants.VOMS_SERVICE_PORT, DEFAULT_PORT);
		shutdownPort = conf.getString(VOMSConfigurationConstants.SHUTDOWN_PORT, DEFAULT_SHUTDOWN_PORT);
		
		certFile = conf.getString(VOMSConfigurationConstants.VOMS_SERVICE_CERT, DEFAULT_CERT);
		keyFile = conf.getString(VOMSConfigurationConstants.VOMS_SERVICE_KEY, DEFAULT_KEY);
		trustDir = conf.getString(VOMSConfigurationConstants.CAFILES, DEFAULT_TRUSTSTORE_DIR);
		trustDirRefreshIntervalInMsec = conf.getLong(VOMSConfigurationConstants.CAFILES_PERIOD, 60000L);
		
	}
	
	public Main(String[] args) {
		
		initOptions();
		parseCommandLineOptions(args);
		loadConfiguration();
		configureJettyServer();
		configureShutdownService();
		start();
	}
	
	
	protected SSLOptions setupSSLOptions(){
		SSLOptions options = new SSLOptions();
		
		options.setCertificateFile(certFile);
		options.setKeyFile(keyFile);
		options.setTrustStoreDirectory(trustDir);
		options.setTrustStoreRefreshIntervalInMsec(trustDirRefreshIntervalInMsec);
		
		return options;
		
	}
	
	protected void setupVOName(){
		
		System.setProperty("VO_NAME", vo);
		
	}
	protected void configureJettyServer(){
		
		server = ServerFactory.newServer(host, 
				Integer.parseInt(port), 
				setupSSLOptions());
		
		vomsWebappContext = new WebAppContext();
		vomsWebappContext.setContextPath(String.format("/voms/%s", vo));
		vomsWebappContext.setWar(war);
		vomsWebappContext.setInitParameter("VO_NAME", vo);
		vomsWebappContext.setInitParameter("CONF_DIR", confDir);
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
