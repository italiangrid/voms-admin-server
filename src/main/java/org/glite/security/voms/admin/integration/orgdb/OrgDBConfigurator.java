package org.glite.security.voms.admin.integration.orgdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.integration.AbstractPluginConfigurator;
import org.glite.security.voms.admin.integration.VOMSPluginConfigurationException;
import org.glite.security.voms.admin.integration.ValidationManager;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBVOMSPersonDAO;
import org.glite.security.voms.admin.integration.orgdb.database.OrgDBError;
import org.glite.security.voms.admin.integration.orgdb.database.OrgDBSessionFactory;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrgDBConfigurator extends AbstractPluginConfigurator{

	public static final Logger log = LoggerFactory.getLogger(OrgDBConfigurator.class);
	
	public static final String DEFAULT_CONFIG_FILE_NAME = "orgdb.properties";
	public static final String ORGDB_EXPERIMENT_NAME_PROPERTY = "experimentName";
	public static final String ORGDB_REGISTRATION_TYPE= "orgdb";
	
	
	/**
	 * Loads the OrgDB hibernate properties.
	 * 
	 * @return the OrgDB hibernate properties
	 * @throws VOMSPluginConfigurationException 
	 */
	Properties loadOrgDBDatabaseProperties() throws VOMSPluginConfigurationException{
		
		String defaultConfigFilePath = getVomsConfigurationDirectoryPath()+ "/" + DEFAULT_CONFIG_FILE_NAME;
		String configFilePath = getPluginProperty("configFile", defaultConfigFilePath);
		
		Properties orgDbProps = new Properties();
		
		try {
			orgDbProps.load(new FileInputStream(new File(configFilePath)));
		
		} catch (FileNotFoundException e) {
			
			String errorMessage = String.format("Configuration file '%s' for plugin '%s' does not exist!",configFilePath, getPluginName());
			throw new VOMSPluginConfigurationException(errorMessage,e);
			
		} catch (IOException e) {
			String errorMessage = String.format("Error reading configuration file '%s' for plugin '%s' does not exist!",configFilePath, getPluginName());
			throw new VOMSPluginConfigurationException(errorMessage,e);
		}
		
		return orgDbProps;
	}
	
	public void checkOrgDBConnection(){
		
		log.debug("Running OrgDB connection check.");
		
		OrgDBVOMSPersonDAO personDAO = OrgDBDAOFactory.instance().getVOMSPersonDAO();
		
		try{
			personDAO.findPersonByEmail("andrea.ceccanti@cnaf.infn.it");
			log.info("Connection to the OrgDB database is active.");
		
		}catch (HibernateException e) {
			log.warn("Error contacting the OrgDB database.");
		}		
	}
	
	
	public void configure() throws VOMSPluginConfigurationException {
		
		log.debug("OrgDB voms configuration started.");
		try{
			
			OrgDBSessionFactory.initialize(loadOrgDBDatabaseProperties());
		
		}catch (OrgDBError e) {
			log.error("Error configuring OrgDB hibernate session factory!",e);
			throw new VOMSPluginConfigurationException("Error initalizing OrgDB hibernate session factory!",e);
		}
		
		log.debug("OrgDB Database properties loaded succesfully.");
		checkOrgDBConnection();
		String voNameCapitalised = VOMSConfiguration.instance().getVOName().toUpperCase();
		
		String experimentName= getPluginProperty(ORGDB_EXPERIMENT_NAME_PROPERTY, voNameCapitalised);
		log.info("Setting OrgDB experiment name: {}", experimentName);
		
		ValidationManager.instance().registerRequestValidator(new OrgDBRequestValidator(experimentName));
		
		VOMSConfiguration.instance().setRegistrationType(ORGDB_REGISTRATION_TYPE);
		
		log.info("OrgDB request validator registered SUCCESSFULLY.");
		
	}

}
