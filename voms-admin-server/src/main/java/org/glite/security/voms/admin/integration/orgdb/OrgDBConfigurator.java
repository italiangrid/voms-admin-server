/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glite.security.voms.admin.integration.orgdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.core.tasks.VOMSExecutorService;
import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.integration.AbstractPluginConfigurator;
import org.glite.security.voms.admin.integration.VOMSPluginConfigurationException;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBVOMSPersonDAO;
import org.glite.security.voms.admin.integration.orgdb.database.OrgDBError;
import org.glite.security.voms.admin.integration.orgdb.database.OrgDBSessionFactory;
import org.glite.security.voms.admin.integration.orgdb.strategies.OrgDBEmailAddressValidationStrategy;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrgDBConfigurator extends AbstractPluginConfigurator {

  public static final Logger log = LoggerFactory
    .getLogger(OrgDBConfigurator.class);

  public static final String DEFAULT_CONFIG_FILE_NAME = "orgdb.properties";

  public static final String ORGDB_EXPERIMENT_NAME_PROPERTY = "experimentName";
  public static final String ORGDB_MEMBERSHIP_CHECK_PERIOD_IN_SECONDS = "membership_check.period";
  public static final String ORGDB_REGISTRATION_TYPE = "orgdb";

  public static final String ORGDB_EMAIL_VALIDATOR_CONFIG_KEY = "orgdb.email.validator";

  /**
   * Default ORGDB membership check period in seconds. (6 hours)
   */
  private static final Long ORGDB_DEFAULT_CHECK_PERIOD = 26100L;

  private OrgDBEmailAddressValidationStrategy emailValidator;

  /**
   * Loads the OrgDB hibernate properties.
   * 
   * @return the OrgDB hibernate properties
   * @throws VOMSPluginConfigurationException
   */
  Properties loadOrgDBDatabaseProperties()
    throws VOMSPluginConfigurationException {

    String defaultConfigFilePath = getVomsConfigurationDirectoryPath() + "/"
      + DEFAULT_CONFIG_FILE_NAME;
    String configFilePath = getPluginProperty("configFile",
      defaultConfigFilePath);

    Properties orgDbProps = new Properties();

    try {
      orgDbProps.load(new FileInputStream(new File(configFilePath)));

    } catch (FileNotFoundException e) {

      String errorMessage = String.format(
        "Configuration file '%s' for plugin '%s' does not exist!",
        configFilePath, getPluginName());
      throw new VOMSPluginConfigurationException(errorMessage, e);

    } catch (IOException e) {
      String errorMessage = String
        .format(
          "Error reading configuration file '%s' for plugin '%s' does not exist!",
          configFilePath, getPluginName());
      throw new VOMSPluginConfigurationException(errorMessage, e);
    }

    return orgDbProps;
  }

  public void checkOrgDBConnection() {

    log.debug("Running OrgDB connection check.");

    OrgDBVOMSPersonDAO personDAO = OrgDBDAOFactory.instance()
      .getVOMSPersonDAO();
    
    try {
      personDAO.findPersonByEmail("andrea.ceccanti@cnaf.infn.it");
      log.info("Connection to the OrgDB database is active.");
      
      // Don't leave a transaction hanging
      OrgDBSessionFactory.commitTransaction();

    } catch (HibernateException e) {
      log.warn("Error contacting the OrgDB database: {}", e.getMessage(), e);
    }
  }

  public synchronized void configure() throws VOMSPluginConfigurationException {

    log.debug("OrgDB voms configuration started.");
    try {

      OrgDBSessionFactory.initialize(loadOrgDBDatabaseProperties());

    } catch (OrgDBError e) {
      log.error("Error configuring OrgDB hibernate session factory!", e);
      throw new VOMSPluginConfigurationException(
        "Error initalizing OrgDB hibernate session factory!", e);
    }

    log.debug("OrgDB Database properties loaded succesfully.");

    checkOrgDBConnection();
    String uppercaseVOName = VOMSConfiguration.instance().getVOName()
      .toUpperCase();

    String experimentName = getPluginProperty(ORGDB_EXPERIMENT_NAME_PROPERTY,
      uppercaseVOName);
    log.info("Setting OrgDB experiment name: {}", experimentName);

    emailValidator = new DefaultEmailValidationStrategy(experimentName);
    OrgDBRequestValidator validator = new OrgDBRequestValidator(experimentName);

    ValidationManager.instance().setRequestValidationContext(validator);

    VOMSConfiguration.instance().setRegistrationType(ORGDB_REGISTRATION_TYPE);
    VOMSConfiguration.instance().setProperty(
      VOMSConfigurationConstants.VOMS_INTERNAL_RO_PERSONAL_INFORMATION,
      Boolean.TRUE);
    VOMSConfiguration.instance().setProperty(
      VOMSConfigurationConstants.VOMS_INTERNAL_RO_MEMBERSHIP_EXPIRATION_DATE,
      Boolean.TRUE);

    log.info("OrgDB request validator registered SUCCESSFULLY.");

    Long checkPeriod;

    try {

      checkPeriod = Long.parseLong(getPluginProperty(
        OrgDBConfigurator.ORGDB_MEMBERSHIP_CHECK_PERIOD_IN_SECONDS,
        ORGDB_DEFAULT_CHECK_PERIOD.toString()));

    } catch (NumberFormatException e) {

      log
        .error(
          "Error parsing OrgDB membership check validity period: {}. Please provide an appropriate number for the OrgDb membership_check.period property!",
          e.getMessage());

      log.error("The default value of {} seconds will be used instead.",
        ORGDB_DEFAULT_CHECK_PERIOD);
      checkPeriod = ORGDB_DEFAULT_CHECK_PERIOD;
    }

    OrgDBMembershipSynchronizationTask syncTask = new OrgDBMembershipSynchronizationTask(
      experimentName, new SuspendInvalidMembersStrategy(),
      new LogOnlyExpiredParticipationStrategy(), new DefaultSyncStrategy());

    OrgDBSyncTaskContainer.INSTANCE.setTask(syncTask);
    
    VOMSExecutorService.instance().startBackgroundTask(syncTask, null,
      checkPeriod);

  }

  public OrgDBEmailAddressValidationStrategy getEmailValidator() {

    return emailValidator;
  }
}
