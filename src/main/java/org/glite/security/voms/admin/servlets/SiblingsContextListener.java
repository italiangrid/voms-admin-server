package org.glite.security.voms.admin.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;


public class SiblingsContextListener implements ServletContextListener {

	private static final Log log = LogFactory
			.getLog(SiblingsContextListener.class);

	public void contextDestroyed(ServletContextEvent ev) {
		
		// Release commons logging
		// see
		// http://wiki.apache.org/jakarta-commons/Logging/FrequentlyAskedQuestions
		// and
		// http://issues.apache.org/bugzilla/show_bug.cgi?id=26372#c15

		java.beans.Introspector.flushCaches();
		LogFactory.release(Thread.currentThread().getContextClassLoader());
		java.beans.Introspector.flushCaches();

	}

	public void contextInitialized(ServletContextEvent ev) {

		try {
			
			initializeVomsProperties(ev);
			findConfiguredVOs(ev);
			loadVersionProperties(ev);
			configureLogging(ev);
			log.info("Siblings context initialized.");
		
		
		} catch (NamingException e) {
			throw new RuntimeException("Error initalizing VOMS properties!", e); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error configuring loggin system!", e);
		}
		

	}
	
	
	public void initializeVomsProperties(ServletContextEvent ev) throws NamingException{
		// Obtain our environment naming context
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");

		String gliteLocationVar = (String) envCtx.lookup("GLITE_LOCATION_VAR");
		
		if (gliteLocationVar == null)
			throw new NamingException("GLITE_LOCATION_VAR property not found in JNDI context!");
		
		ev.getServletContext().setAttribute("GLITE_LOCATION_VAR", gliteLocationVar);
		
	}
	
	
	protected void configureLogging(ServletContextEvent ev) throws IOException{
		
		Properties log4jProps = new Properties();

		InputStream is = ev.getServletContext()
				.getResourceAsStream("/WEB-INF/classes/log4j.siblings.properties");
		
		
		if (is == null)
			throw new RuntimeException("Error configuring logging system: log4j.siblings.properties not found in application context!");
		
		log4jProps.load(is);
		
		LogManager.resetConfiguration();
		PropertyConfigurator.configure(log4jProps);
	
	}
	
	public void loadVersionProperties(ServletContextEvent ev) throws IOException{
		
		Properties versionProps = new Properties();
		InputStream is = ev.getServletContext()
		.getResourceAsStream("/WEB-INF/classes/version.properties");
		
		versionProps.load(is);
		
		ev.getServletContext().setAttribute("version", versionProps.get("voms-admin.server.version"));
		
		
	}
	
	public void findConfiguredVOs(ServletContextEvent ev) {

		String configDirPath = (String) ev.getServletContext().getAttribute("GLITE_LOCATION_VAR");

		if (configDirPath == null)
			throw new RuntimeException(
					"No value found for GLITE_LOCATION_VAR!");

		
		List voList = new ArrayList();

		File configDir = new File(configDirPath + File.separator + "etc"
				+ File.separator + "voms-admin");

		if (!configDir.exists())
			throw new RuntimeException(
					"Voms configuration directory does not exist");

		File[] filez = configDir.listFiles();

		if (filez != null)
			for (int i = 0; i < filez.length; i++) {

				if (filez[i].isDirectory()) {
					log.debug("Found vo: " + filez[i].getName());
					voList.add(filez[i].getName());
				}

			}
		
		ev.getServletContext().setAttribute("configuredVOs", voList);
		
	}
}


