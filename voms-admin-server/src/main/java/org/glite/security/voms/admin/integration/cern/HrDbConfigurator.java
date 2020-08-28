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
package org.glite.security.voms.admin.integration.cern;

import static java.util.Objects.isNull;
import static org.glite.security.voms.admin.configuration.VOMSConfigurationConstants.PI_REQUIRED_FIELDS;
import static org.glite.security.voms.admin.configuration.VOMSConfigurationConstants.VOMS_INTERNAL_RO_MEMBERSHIP_EXPIRATION_DATE;
import static org.glite.security.voms.admin.configuration.VOMSConfigurationConstants.VOMS_INTERNAL_RO_PERSONAL_INFORMATION;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.core.tasks.DatabaseTransactionTaskWrapper;
import org.glite.security.voms.admin.core.tasks.VOMSExecutorService;
import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.integration.AbstractPluginConfigurator;
import org.glite.security.voms.admin.integration.VOMSPluginConfigurationException;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HrDbConfigurator extends AbstractPluginConfigurator
    implements HrDbRequestValidatorFactory {

  public static final Logger LOG = LoggerFactory.getLogger(HrDbConfigurator.class);

  public static final String DEFAULT_CONFIG_FILE_NAME = "hr.properties";

  public static final String HR_DB_REGISTRATION_TYPE = "hr";

  private Clock clock;
  private VOMSExecutorService executorService;
  private VOMSUserDAO userDao;
  private ValidationManager validationManager;
  private HrDbApiServiceFactory apiServiceFactory;
  private HrDbSyncTaskFactory syncTaskFactory;
  private HrDbRequestValidatorFactory requestValidatorFactory;
  private HrDbProperties hrConfig;

  private HrDbApiService apiService;

  public HrDbConfigurator(VOMSConfiguration config) {
    super(config);
  }

  private HrDbProperties loadHrDBProperties() throws VOMSPluginConfigurationException {

    String defaultConfigFilePath =
        getVomsConfigurationDirectoryPath() + "/" + DEFAULT_CONFIG_FILE_NAME;
    String configFilePath = getPluginProperty("configFile", defaultConfigFilePath);

    Properties hrDbProps = new Properties();

    try {
      hrDbProps.load(new FileInputStream(new File(configFilePath)));

    } catch (IOException e) {

      String errorMessage = String.format("Configuration file '%s' for plugin '%s' does not exist!",
          configFilePath, getPluginName());
      throw new VOMSPluginConfigurationException(errorMessage, e);

    }

    return HrDbProperties.fromProperties(hrDbProps);
  }

  Runnable wrapTask(Runnable task) {
    return new DatabaseTransactionTaskWrapper(task, true, true);
  }

  void initDependencies() throws VOMSPluginConfigurationException {

    if (isNull(clock)) {
      clock = Clock.systemDefaultZone();
    }

    if (isNull(apiServiceFactory)) {
      apiServiceFactory = new HttpClientHrDbApiServiceFactory();
    }

    if (isNull(executorService)) {
      executorService = VOMSExecutorService.instance();
    }

    if (isNull(validationManager)) {
      validationManager = ValidationManager.instance();
    }

    if (isNull(userDao)) {
      userDao = VOMSUserDAO.instance();
    }

    if (isNull(hrConfig)) {
      hrConfig = loadHrDBProperties();
    }

    if (isNull(requestValidatorFactory)) {
      requestValidatorFactory = this;
    }

    if (isNull(syncTaskFactory)) {
      syncTaskFactory = new DefaultHrDbSyncTaskFactory(clock, validationManager);
    }
  }

  private void scheduleSyncTask(HrDbProperties config, Runnable syncTask) {

    final Runnable task = wrapTask(syncTask);
    ZonedDateTime now = ZonedDateTime.now(clock);
    ZonedDateTime nextRun =
        now.withHour(config.getMembesrshipCheck().getStartHour()).withMinute(0).withSecond(0);

    if (now.compareTo(nextRun) > 0) {
      nextRun = nextRun.plusDays(1);
    }

    LOG.info("Scheduling HR DB sync task to start on {} and run every {} seconds", nextRun,
        config.getMembesrshipCheck().getPeriodInSeconds());

    Duration duration = Duration.between(now, nextRun);

    executorService.scheduleAtFixedRate(task, duration.getSeconds(),
        config.getMembesrshipCheck().getPeriodInSeconds(), TimeUnit.SECONDS);

    if (config.getMembesrshipCheck().isRunAtStartup()) {

      ZonedDateTime startupRun = now.plusMinutes(5);

      if (Duration.between(startupRun, nextRun).compareTo(Duration.ofMinutes(30)) <= 0) {
        LOG.info(
            "Not scheduling the HR DB sync task startup run, as the periodic run is scheduled within 30 minutes");
      } else {
        LOG.info(
            "Scheduling a startup run of the HR DB sync task in 5 minutes as requested by configuration");
        executorService.schedule(task, 300, TimeUnit.SECONDS);
      }
    }
  }

  @Override
  public void configure() throws VOMSPluginConfigurationException {
    LOG.debug("HR DB voms plugin configuration started");

    initDependencies();

    VOMSConfiguration config = getVomsConfig();
    config.setRegistrationType(HR_DB_REGISTRATION_TYPE);
    config.setProperty(VOMS_INTERNAL_RO_PERSONAL_INFORMATION, Boolean.TRUE);
    config.setProperty(VOMS_INTERNAL_RO_MEMBERSHIP_EXPIRATION_DATE, Boolean.TRUE);

    config.setProperty(PI_REQUIRED_FIELDS, "");

    apiService = apiServiceFactory.newHrDbApiService(hrConfig);

    validationManager.setRequestValidationContext(
        requestValidatorFactory.newHrDbRequestValidator(hrConfig, apiService));

    LOG.info("HR DB request validator registered succesfully. Syncing against HR db experiment {}",
        hrConfig.getExperimentName());

    if (hrConfig.getMembesrshipCheck().isEnabled()) {
      scheduleSyncTask(hrConfig,
          syncTaskFactory.buildSyncTask(hrConfig, apiService, userDao, config));
    } else {
      LOG.info("HR DB sync task DISABLED as requested by configuration");
    }

    LOG.info("HR DB voms plugin started");

  }

  public void setExecutorService(VOMSExecutorService executorService) {
    this.executorService = executorService;
  }

  public void setValidationManager(ValidationManager validationManager) {
    this.validationManager = validationManager;
  }

  public void setApiServiceFactory(HrDbApiServiceFactory apiServiceFactory) {
    this.apiServiceFactory = apiServiceFactory;
  }

  public void setUserDao(VOMSUserDAO userDao) {
    this.userDao = userDao;
  }

  public void setSyncTaskFactory(HrDbSyncTaskFactory syncTaskFactory) {
    this.syncTaskFactory = syncTaskFactory;
  }

  public void setRequestValidatorFactory(HrDbRequestValidatorFactory requestValidatorFactory) {
    this.requestValidatorFactory = requestValidatorFactory;
  }

  public void setClock(Clock clock) {
    this.clock = clock;
  }

  public void setHrConfig(HrDbProperties hrConfig) {
    this.hrConfig = hrConfig;
  }

  @Override
  public HrDbRequestValidator newHrDbRequestValidator(HrDbProperties properties,
      HrDbApiService api) {

    return new HrDbRequestValidator(clock, properties, api);
  }

  public HrDbApiService getApiService() {
    return apiService;
  }

  public HrDbProperties getHrConfig() {
    return hrConfig;
  }
}
