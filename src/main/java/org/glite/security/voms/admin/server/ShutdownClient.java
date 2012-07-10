package org.glite.security.voms.admin.server;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

/***
 * The VOMS shutdown client. This client executes an HTTP GET for /shutdown on localhost on a given port.
 */
public class ShutdownClient {
	
	private static final String ARG_VO = "vo";
	private static final String ARG_VO_DESC = "The VO to be shut down.";
	
	private static final String ARG_PORT = "port";
	private static final String ARG_PORT_DESC = "The port where the shutdown service is listening.";
	
	private static final String ARG_PASSWORD = "password";
	private static final String ARG_PASSWORD_DESC = "The password used to authenticate at the shutdown service.";
	
	/** The port where the shutdown service is listening **/ 
	private int port;
	
	/** The password used to authenticate at the shutdown service **/
	private String password;
	
	/** The CLI options **/ 
	private static Options cliOptions = new Options();
	
	/** The CLI options parser **/
	private static CommandLineParser parser = new GnuParser();
	
	
	/**
	 * Initializes the CLI parsing options
	 */
	private static void initOptions(){
		
		OptionGroup og = new OptionGroup();
		og.addOption(new Option(ARG_VO, true, ARG_VO_DESC));
		og.addOption(new Option(ARG_PORT, true, ARG_PORT_DESC));
		
		cliOptions.addOptionGroup(og);
		
		cliOptions.addOption(new Option(ARG_PASSWORD, true, ARG_PASSWORD_DESC));
		
	}
	

	/**
	 * Constructor
	 * 
	 * @param shutdownPort the port where the shutdown service is listening
	 * @param shutdownPassword the password used to authenticate at the shutdown service
	 */
	public ShutdownClient(int shutdownPort, String shutdownPassword) {
		
		this.port = shutdownPort;
		
		if (shutdownPassword != null){
			this.password = shutdownPassword.trim();
		}
	}
	
	/** Disables logging messages (logback version) */
    private static void disableLogBackLibraryLogging() {
    	LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger rootLogger = lc.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		rootLogger.setLevel(Level.OFF);
    }
	
	/**
	 * Executes the shutdown procedure
	 */
	public void doShutdown(){
		
		String shutdownEndpoint = String.format("http://localhost:%d/shutdown", port);
		
		if (password != null)
			shutdownEndpoint+=String.format("?password=%s",password);
		
		HttpClient httpClient = new HttpClient();
		GetMethod shutdown = new GetMethod(shutdownEndpoint);
		
		try{
			
			httpClient.executeMethod(shutdown);
		
		}catch (Exception e) {
			System.err.println("VOMS shutdown error. "+e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Prints usage information
	 */
	static void usage(){
		
		HelpFormatter formatter = new HelpFormatter();
		
		formatter.printHelp("java -cp ... org.glite.security.voms.admin.server.ShutdownClient", cliOptions);

	}
	
	/**
	 * Creates a {@link ShutdownClient} starting from the properties specified in a VO configuration
	 * @param voName the name of the VO for which the {@link ShutdownClient} should be created
	 * @return a {@link ShutdownClient} that can shut down the VO passed as argument 
	 */
	protected static ShutdownClient getShutdownClientForVO(String voName){
		
		System.setProperty(VOMSConfigurationConstants.VO_NAME, voName);
		
		VOMSConfiguration conf = VOMSConfiguration.load(null);
		conf.loadServiceProperties();
		
		String port = conf.getString(VOMSConfigurationConstants.SHUTDOWN_PORT);
		String password = conf.getString(VOMSConfigurationConstants.SHUTDOWN_PASSWORD);
		
		if (port == null){
			
			System.err.format("Shutdown port property not found in VO %s configuration!", voName);
			System.exit(1);
		}
		
		int portNumber = checkPortNumber(port);
		
		return new ShutdownClient(portNumber, password);
	}
	
	protected static int checkPortNumber(String portNumberString){
		
		int thePort = -1;
		
		try{
			
			thePort = Integer.parseInt(portNumberString);
			
			if (thePort > 65536 || thePort < 0){
				
				System.err.print("Please provide a valid shutdown port! (0 < port < 65536)");
				System.exit(1);
			}
			
		}catch (NumberFormatException e){
			
			System.err.print("Error parsing the shutdown port number (not an integer). "+e.getMessage());
			System.exit(-1);
		}
		
		return thePort;
	}
	
	public static void main(String[] args) {
		
		disableLogBackLibraryLogging();
		initOptions();
		
		ShutdownClient sc = null;
		CommandLine cmdLine = null;
				
		try{	
			
			cmdLine = parser.parse(cliOptions, args);
		
		}catch (ParseException e) {
			System.err.println("Error parsing command line arguments: "+e.getMessage());
			usage();
			System.exit(1);
		}	
		
		
		String vo = cmdLine.getOptionValue(ARG_VO); 
		String port = cmdLine.getOptionValue(ARG_PORT);
		String password = cmdLine.getOptionValue(ARG_PASSWORD);
			
		if (vo != null)
			sc = getShutdownClientForVO(vo);
			
		if (port != null){
			int portNumber = checkPortNumber(port);		
			sc = new ShutdownClient(portNumber, password);
		}
		
		sc.doShutdown();
		
	}

}
