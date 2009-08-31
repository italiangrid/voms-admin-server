/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.JNDIConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

public final class VOMSConfiguration {

	public static final String LOCALHOST_DEFAULTS_TO_LOCAL_ADMIN = "voms.localhost.defaults.to.local.admin";

	public static final String READONLY = "voms.readonly";

	public static final String CAFILES = "voms.cafiles";

	public static final String CAFILES_PERIOD = "voms.cafiles.period";

	public static final String CREATE_EXPIRED_CAS = "voms.ca.create-when-expired";

	public static final String DROP_EXPIRED_CAS = "voms.ca.drop-when-expired";

	public static final String AUDITING = "voms.auditing";

	public static final String AUDITING_INSERTS = "voms.auditing.inserts";

	public static final String AUDITING_UPDATES = "voms.auditing.updates";

	public static final String AUDITING_DELETIONS = "voms.auditing.deletions";

	public static final String SERVICE_EMAIL_ADDRESS = "voms.notification.email-address";

	public static final String SERVICE_SMTP_SERVER = "voms.notification.smtp-server";

	public static final String SERVICE_SMTP_SERVER_PORT = "voms.notification.smtp-server.port";

	public static final String SERVICE_EMAIL_ACCOUNT_USERNAME = "voms.notification.username";

	public static final String SERVICE_EMAIL_ACCOUNT_PASSWORD = "voms.notification.password";

	public static final String SERVICE_EMAIL_USE_TLS = "voms.notification.use_tls";

	/**
	 * VO Membership requests expiration time (in minutes).
	 */
	public static final String VO_MEMBERSHIP_EXPIRATION_TIME = "voms.request.vo-membership-expiration-time";

	public static final String VO_NAME = "voms.vo.name";

	public static final String USER_MAX_RESULTS_PER_PAGE = "voms.pagination.user.max.results.per.page";

	public static final String ATTRIBUTES_MAX_RESULTS_PER_PAGE = "voms.pagination.attributes.max.results.per.page";

	public static final String GROUP_MAX_RESULTS_PER_PAGE = "voms.pagination.group.max.results.per.page";

	public static final String ROLE_MAX_RESULTS_PER_PAGE = "voms.pagination.role.max.results.per.page";

	public static final String REGISTRATION_SERVICE_ENABLED = "voms.request.webui.enabled";

	public static final String READ_ACCESS_FOR_AUTHENTICATED_CLIENTS = "voms.read-access-for-authenticated-clients";

	/**
	 * VERSION Properties
	 */

	public static final String VOMS_ADMIN_SERVER_VERSION = "voms-admin.server.version";
	public static final String VOMS_ADMIN_INTERFACE_VERSION = "voms-admin.interface.version";

	public static final String NOTIFICATION_RETRY_PERIOD = "voms.notification.retry_period";

	/**
	 * AUP Properties
	 */
	public static final String GRID_AUP_URL = "voms.aup.grid_aup.initial_url";
	public static final String VO_AUP_URL = "voms.aup.vo_aup.initial_url";
	public static final String SIGN_AUP_TASK_LIFETIME = "voms.aup.sign_aup_task_lifetime";

	/**
	 * Membership Properties
	 */
	public static final String DEFAULT_MEMBERSHIP_LIFETIME = "voms.membership.default_lifetime";

	public static final String MEMBERSHIP_CHECK_PERIOD = "voms.task.membership_check.period";

	// Test property

	public static final String TEST_VO_NAME = "test_vo_mysql";

	Log log = LogFactory.getLog(VOMSConfiguration.class);

	private static VOMSConfiguration instance = null;

	private ServletContext context;

	private CompositeConfiguration config;

	private static final String[] voRuntimeProperties = new String[] {
			"VO_NAME", "GLITE_LOCATION", "GLITE_LOCATION_VAR", "VOMS_LOCATION" };

	private void configureLogging() {

		if (context != null) {

			try {

				Properties log4jProps = new Properties();

				InputStream is = context
						.getResourceAsStream("/WEB-INF/classes/log4j.runtime.properties");

				if (is == null)
					throw new VOMSConfigurationException(
							"Error configuring logging system: log4j.runtime.properties not found in application context!");

				log4jProps.load(is);
				log4jProps.setProperty("log4j.vo", getVOName());

				LogManager.resetConfiguration();
				PropertyConfigurator.configure(log4jProps);

			} catch (MalformedURLException e) {

				log.error("Error configuring logging properties! "
						+ e.getMessage(), e);

			} catch (IOException e) {
				log.error("Error configuring logging properties! "
						+ e.getMessage(), e);

				throw new VOMSConfigurationException(e.getMessage(), e);
			}

		}

	}

	private void loadDatabaseProperties() {

		PropertiesConfiguration vomsDatabaseProperties;
		try {
			vomsDatabaseProperties = new PropertiesConfiguration(
					getVomsDatabasePropertiesFileName());

			config.addConfiguration(vomsDatabaseProperties);

		} catch (ConfigurationException e) {
			log.fatal("Error loading voms.database.properties: "
					+ e.getMessage());

			if (log.isDebugEnabled())
				log.fatal(e.getMessage(), e);

			throw new VOMSConfigurationException(
					"Error loading voms.database.properties: " + e.getMessage(),
					e);

		}

		log.debug("VOMS Admin database properties loaded!");
	}

	private void loadServiceProperties() {

		String fileName = getVomsServicePropertiesFileName();

		try {

			PropertiesConfiguration vomsServiceProperties = new PropertiesConfiguration(
					getVomsServicePropertiesFileName());
			config.addConfiguration(vomsServiceProperties);

		} catch (ConfigurationException e) {
			log.fatal("Error loading service properties from " + fileName);

			if (log.isDebugEnabled())
				log.fatal(e.getMessage(), e);

			throw new VOMSConfigurationException(
					"Error loading service properties from " + fileName, e);

		}

		log.debug("VOMS Admin service properties loaded!");

	}

	private void loadVORuntimeProperties() {

		JNDIConfiguration jndiConfig;

		try {

			jndiConfig = new JNDIConfiguration();

		} catch (NamingException e) {
			log.fatal("Error accessing JNDI configuration properties: "
					+ e.getMessage());
			if (log.isDebugEnabled())
				log.fatal(e.getMessage(), e);

			throw new VOMSConfigurationException(
					"Error accessing JNDI configuration properties: "
							+ e.getMessage(), e);

		}

		SystemConfiguration systemConfig = new SystemConfiguration();

		if (log.isDebugEnabled()) {

			log.debug("JNDI Configuration contents: ");
			Iterator jndiKeysIter = jndiConfig.getKeys();

			while (jndiKeysIter.hasNext()) {

				String key = (String) jndiKeysIter.next();
				log.debug(key + " = " + jndiConfig.getProperty(key));

			}

			log.debug("System Configuration contents: ");
			Iterator systemKeysIter = systemConfig.getKeys();

			while (systemKeysIter.hasNext()) {

				String key = (String) systemKeysIter.next();
				log.debug(key + " = " + systemConfig.getProperty(key));
			}
		}

		jndiConfig.setPrefix("java:comp/env");

		// Add values coming from JNDI context
		config.addConfiguration(jndiConfig);

		for (int i = 0; i < voRuntimeProperties.length; i++)
			if (!config.containsKey(voRuntimeProperties[i])) {
				log
						.debug("VO runtime property '"
								+ voRuntimeProperties[i]
								+ "' not found in JNDI context! Checking system properties for fallback value!");

				if (!systemConfig.containsKey(voRuntimeProperties[i]))
					throw new VOMSConfigurationException(
							"Error loading voms-admin configuration: '"
									+ voRuntimeProperties[i]
									+ "' VO runtime property, needed to properly initialize voms-admin services, was not found "
									+ "in JNDI naming service and in the process environment!");

				// Property value found in environment
				config.setProperty(voRuntimeProperties[i], systemConfig
						.getString(voRuntimeProperties[i]));
				log.debug("VO runtime property '" + voRuntimeProperties[i]
						+ "' found in the system properties with value: '"
						+ systemConfig.getString(voRuntimeProperties[i]));

			}

		// Rename the VO_NAME property for internal use
		// FIXME: is this really needed?
		config.setProperty(VO_NAME, config.getString("VO_NAME"));

	}

	private void loadVersionProperties() {

		InputStream versionPropStream = this.getClass().getClassLoader()
				.getResourceAsStream("version.properties");
		PropertiesConfiguration versionProperties = new PropertiesConfiguration();

		try {
			versionProperties.load(versionPropStream);

			config.addConfiguration(versionProperties);

		} catch (ConfigurationException e) {

			log.error("Error configuring version properties:" + e.getMessage(),
					e);
			throw new VOMSConfigurationException(
					"Error configuring version properties!", e);

		}
	}

	private VOMSConfiguration(boolean useJNDI, ServletContext context) {

		this.context = context;

		config = new CompositeConfiguration();

		if (useJNDI)
			loadVORuntimeProperties();
		else
			config.addConfiguration(new SystemConfiguration());

		configureLogging();

		if (!getVOName().equals("siblings"))
			loadServiceProperties();

		loadVersionProperties();

		log.info("VOMS-Admin configuration loaded!");

	}

	private String getVomsServicePropertiesFileName() {

		String fileName = config.getString("GLITE_LOCATION_VAR")
				+ "/etc/voms-admin/" + getVOName() + "/voms.service.properties";

		return fileName;

	}

	private String getVomsDatabasePropertiesFileName() {

		String fileName = config.getString("GLITE_LOCATION_VAR")
				+ "/etc/voms-admin/" + getVOName()
				+ "/voms.database.properties";

		log.debug("voms.database.properties=" + fileName);

		return fileName;

	}

	public Properties getDatabaseProperties() {

		String propFileName = getString("GLITE_LOCATION_VAR")
				+ "/etc/voms-admin/" + getVOName()
				+ "/voms.database.properties";

		Properties props = new Properties();

		try {
			props.load(new FileInputStream(propFileName));

		} catch (IOException e) {

			log
					.fatal("Error loading database properties: "
							+ e.getMessage(), e);
			throw new VOMSException("Error loading database properties: "
					+ e.getMessage(), e);
		}

		return props;

	}

	private String loadVomsesConfigurationString() {

		String vomsesConfFileName = getString("GLITE_LOCATION_VAR")
				+ "/etc/voms-admin/" + getVOName() + "/vomses";

		try {

			StringBuffer vomsesContent = new StringBuffer();
			FileReader vomsesFileReader = new FileReader(vomsesConfFileName);

			int c;
			while ((c = vomsesFileReader.read()) != -1)
				vomsesContent.append((char) c);

			return vomsesContent.toString();

		} catch (IOException e) {

			log.fatal("Error loading vomses configuration file:"
					+ e.getMessage(), e);
			throw new VOMSException("Error loading vomses configuration file:"
					+ e.getMessage(), e);
		}

	}

	public static VOMSConfiguration instance() {

		return instance(true, null);

	}

	public static VOMSConfiguration instance(ServletContext context) {

		return instance(true, context);

	}

	public static VOMSConfiguration instance(boolean useJNDI,
			ServletContext context) {

		if (instance == null)
			instance = new VOMSConfiguration(useJNDI, context);

		return instance;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#addProperty(java.lang.
	 * String, java.lang.Object)
	 */
	public void addProperty(String arg0, Object arg1) {

		config.addProperty(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.configuration.Configuration#clear()
	 */
	public void clear() {

		config.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#clearProperty(java.lang
	 * .String)
	 */
	public void clearProperty(String arg0) {

		config.clearProperty(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#containsKey(java.lang.
	 * String)
	 */
	public boolean containsKey(String arg0) {

		return config.containsKey(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getBigDecimal(java.lang
	 * .String, java.math.BigDecimal)
	 */
	public BigDecimal getBigDecimal(String arg0, BigDecimal arg1) {

		return config.getBigDecimal(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getBigDecimal(java.lang
	 * .String)
	 */
	public BigDecimal getBigDecimal(String arg0) {

		return config.getBigDecimal(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getBigInteger(java.lang
	 * .String, java.math.BigInteger)
	 */
	public BigInteger getBigInteger(String arg0, BigInteger arg1) {

		return config.getBigInteger(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getBigInteger(java.lang
	 * .String)
	 */
	public BigInteger getBigInteger(String arg0) {

		return config.getBigInteger(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getBoolean(java.lang.String
	 * , boolean)
	 */
	public boolean getBoolean(String arg0, boolean arg1) {

		return config.getBoolean(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getBoolean(java.lang.String
	 * , java.lang.Boolean)
	 */
	public Boolean getBoolean(String arg0, Boolean arg1) {

		return config.getBoolean(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getBoolean(java.lang.String
	 * )
	 */
	public boolean getBoolean(String arg0) {

		return config.getBoolean(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getByte(java.lang.String,
	 * byte)
	 */
	public byte getByte(String arg0, byte arg1) {

		return config.getByte(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getByte(java.lang.String,
	 * java.lang.Byte)
	 */
	public Byte getByte(String arg0, Byte arg1) {

		return config.getByte(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getByte(java.lang.String)
	 */
	public byte getByte(String arg0) {

		return config.getByte(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getDouble(java.lang.String
	 * , double)
	 */
	public double getDouble(String arg0, double arg1) {

		return config.getDouble(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getDouble(java.lang.String
	 * , java.lang.Double)
	 */
	public Double getDouble(String arg0, Double arg1) {

		return config.getDouble(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getDouble(java.lang.String
	 * )
	 */
	public double getDouble(String arg0) {

		return config.getDouble(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getFloat(java.lang.String,
	 * float)
	 */
	public float getFloat(String arg0, float arg1) {

		return config.getFloat(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getFloat(java.lang.String,
	 * java.lang.Float)
	 */
	public Float getFloat(String arg0, Float arg1) {

		return config.getFloat(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getFloat(java.lang.String)
	 */
	public float getFloat(String arg0) {

		return config.getFloat(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getInt(java.lang.String,
	 * int)
	 */
	public int getInt(String arg0, int arg1) {

		return config.getInt(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getInt(java.lang.String)
	 */
	public int getInt(String arg0) {

		return config.getInt(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getInteger(java.lang.String
	 * , java.lang.Integer)
	 */
	public Integer getInteger(String arg0, Integer arg1) {

		return config.getInteger(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.configuration.Configuration#getKeys()
	 */
	public Iterator getKeys() {

		return config.getKeys();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getKeys(java.lang.String)
	 */
	public Iterator getKeys(String arg0) {

		return config.getKeys(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getList(java.lang.String,
	 * java.util.List)
	 */
	public List getList(String arg0, List arg1) {

		return config.getList(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getList(java.lang.String)
	 */
	public List getList(String arg0) {

		return config.getList(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getLong(java.lang.String,
	 * long)
	 */
	public long getLong(String arg0, long arg1) {

		return config.getLong(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getLong(java.lang.String,
	 * java.lang.Long)
	 */
	public Long getLong(String arg0, Long arg1) {

		return config.getLong(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getLong(java.lang.String)
	 */
	public long getLong(String arg0) {

		return config.getLong(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getProperties(java.lang
	 * .String)
	 */
	public Properties getProperties(String arg0) {

		return config.getProperties(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getProperty(java.lang.
	 * String)
	 */
	public Object getProperty(String arg0) {

		return config.getProperty(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getShort(java.lang.String,
	 * short)
	 */
	public short getShort(String arg0, short arg1) {

		return config.getShort(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getShort(java.lang.String,
	 * java.lang.Short)
	 */
	public Short getShort(String arg0, Short arg1) {

		return config.getShort(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getShort(java.lang.String)
	 */
	public short getShort(String arg0) {

		return config.getShort(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getString(java.lang.String
	 * , java.lang.String)
	 */
	public String getString(String arg0, String arg1) {

		return config.getString(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getString(java.lang.String
	 * )
	 */
	public String getString(String arg0) {

		return config.getString(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#getStringArray(java.lang
	 * .String)
	 */
	public String[] getStringArray(String arg0) {

		return config.getStringArray(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.configuration.Configuration#isEmpty()
	 */
	public boolean isEmpty() {

		return config.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#setProperty(java.lang.
	 * String, java.lang.Object)
	 */
	public void setProperty(String arg0, Object arg1) {

		config.setProperty(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.configuration.Configuration#subset(java.lang.String)
	 */
	public Configuration subset(String arg0) {

		return config.subset(arg0);
	}

	public Map asMap() {

		HashMap returnValue = new HashMap();
		Iterator keys = config.getKeys();
		while (keys.hasNext()) {
			Object key = keys.next();
			Object property = config.getProperty((String) key);
			returnValue.put(key, property);

		}

		return returnValue;
	}

	public String getVOName() {

		return getString(VO_NAME);
	}

	public List getLocallyConfiguredVOs() {

		String configDirPath = getString("GLITE_LOCATION_VAR");

		if (configDirPath == null)
			throw new VOMSConfigurationException(
					"No value found for GLITE_LOCATION_VAR!");

		List voList = new ArrayList();

		File configDir = new File(configDirPath + File.separator + "etc"
				+ File.separator + "voms-admin");

		if (!configDir.exists())
			throw new VOMSConfigurationException(
					"Voms configuration directory does not exist");

		File[] filez = configDir.listFiles();

		if (filez != null)
			for (int i = 0; i < filez.length; i++) {

				if (filez[i].isDirectory()) {
					log.debug("Found vo: " + filez[i].getName());
					voList.add(filez[i].getName());
				}

			}

		return voList;

	}

	
	public String getVomsesConfigurationString() {

		if (getString("vomses.configuration") == null) {
			String vomsesConfString = loadVomsesConfigurationString();
			setProperty("vomses.configuration", vomsesConfString);
		}

		return getString("vomses.configuration");

	}

	public String getTemplatePath() {

		return getString("GLITE_LOCATION") + "/etc/voms-admin/templates/email";
	}

	private String getCustomizedContentPath() {

		String pathName = config.getString("GLITE_LOCATION_VAR")
				+ "/etc/voms-admin/" + getVOName() + "/customized-content/";
		return pathName;
	}

	public boolean pageHasCustomization(String pageName) {

		String basePath = getCustomizedContentPath() + "/" + pageName;
		File f = new File(basePath);

		return (f.exists() && f.canRead());

	}

	public String getCustomizationPageAbsolutePath(String pageName) {
		String basePath = getCustomizedContentPath() + "/" + pageName;
		File f = new File(basePath);

		if (f.exists() && f.canRead())
			return f.getAbsolutePath();

		return null;

	}

	public String getDefaultGridAUPURL() {
		return String.format("file://%s/etc/voms-admin/%s/grid-aup.txt",
				getString("GLITE_LOCATION_VAR"), getVOName());
	}

	public String getDefaultVOAUPURL() {
		return String.format("file://%s/etc/voms-admin/%s/vo-aup.txt",
				getString("GLITE_LOCATION_VAR"), getVOName());
	}

	public ServletContext getServletContext() {
		return context;
	}
}
