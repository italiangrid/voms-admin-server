/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.core;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.apache.commons.lang.Validate;
import org.apache.velocity.app.Velocity;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.configuration.VOMSConfigurationException;
import org.glite.security.voms.admin.core.tasks.ExpiredRequestsPurgerTask;
import org.glite.security.voms.admin.core.tasks.PrintX509AAStatsTask;
import org.glite.security.voms.admin.core.tasks.SignAUPReminderCheckTask;
import org.glite.security.voms.admin.core.tasks.SystemTimeProvider;
import org.glite.security.voms.admin.core.tasks.TaskStatusUpdater;
import org.glite.security.voms.admin.core.tasks.ThreadUncaughtExceptionHandler;
import org.glite.security.voms.admin.core.tasks.UpdateCATask;
import org.glite.security.voms.admin.core.tasks.UserStatsTask;
import org.glite.security.voms.admin.core.tasks.VOMSExecutorService;
import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.error.VOMSFatalException;
import org.glite.security.voms.admin.event.DebugEventLogListener;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.auditing.AuditLog;
import org.glite.security.voms.admin.integration.PluginManager;
import org.glite.security.voms.admin.notification.NotificationService;
import org.glite.security.voms.admin.notification.dispatchers.CertificateRequestsNotificationDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.DefaultNotificationDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.GroupMembershipNotificationDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.MembershipRemovalNotificationDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.RoleMembershipNotificationDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.UserSuspendedDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.VOMembershipNotificationDispatcher;
import org.glite.security.voms.admin.operations.DefaultPrincipalProvider;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.VOMSVersionDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.italiangrid.voms.aa.x509.ACGeneratorFactory;
import org.opensaml.xml.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public final class VOMSService {

  public static final String ENDPOINTS_KEY = "__voms_endpoints";

  static final Logger log = LoggerFactory.getLogger(VOMSService.class);

  protected static void checkDatabaseVersion() {

    int version = VOMSVersionDAO.instance().getVersion().getVersion();

    if (version != VOMSServiceConstants.VOMS_DB_VERSION) {

      if (version < VOMSServiceConstants.VOMS_DB_VERSION) {
        log
          .error("VOMS DATABASE SCHEMA ERROR: old voms database schema found: version "
            + version);
        log
          .error("PLEASE UPGRADE TO CURRENT VERSION, usign voms-admin-configure or voms-db-util commands!");
        throw new VOMSFatalException("INCOMPATIBLE DATABASE SCHEMA FOUND! Is '"
          + version + "', while it should be '"
          + VOMSServiceConstants.VOMS_DB_VERSION + "'");
      } else {

        log.error("UNKNOWN SCHEMA VERSION NUMBER FOUND IN DATABASE! version: "
          + version);
        throw new VOMSFatalException("INCOMPATIBLE DATABASE SCHEMA FOUND! Is '"
          + version + "', while it should be '"
          + VOMSServiceConstants.VOMS_DB_VERSION + "'");
      }
    }
  }

  protected static void configureVelocity() {

    try {

      Properties p = new Properties();

      p.put("resource.loader", "cpath");
      p.put("cpath.resource.loader.class",
        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

      p.put("runtime.log.logsystem.class",
        "org.glite.security.voms.admin.util.velocity.VelocityLogger");

      Velocity.init(p);
      log.debug("Velocity setup ok!");

    } catch (Exception e) {

      log.error("Error initializing velocity template engine!");
      throw new VOMSFatalException(e);
    }

  }

  protected static void configureEventManager() {

    EventManager manager = EventManager.instance();

    AuditLog.INSTANCE.setPrincipalProvider(new DefaultPrincipalProvider());
    AuditLog.INSTANCE.setDaoFactory(DAOFactory.instance());

    manager.register(AuditLog.INSTANCE);
    
    manager.register(DebugEventLogListener.instance());
    manager.register(UserSuspendedDispatcher.instance());
    manager.register(DefaultNotificationDispatcher.instance());
    manager.register(GroupMembershipNotificationDispatcher.instance());
    manager.register(RoleMembershipNotificationDispatcher.instance());
    manager.register(VOMembershipNotificationDispatcher.instance());
    manager.register(CertificateRequestsNotificationDispatcher.instance());
    manager.register(MembershipRemovalNotificationDispatcher.instance());
    
  }
  
  protected static void startBackgroundTasks() {

    VOMSExecutorService es = VOMSExecutorService.instance();

    VOMSConfiguration conf = VOMSConfiguration.instance();
    List<Integer> aupReminders = conf.getAUPReminderIntervals();
    
    es.startBackgroundTask(new SignAUPReminderCheckTask(
      DAOFactory.instance(),
      EventManager.instance(),
      SystemTimeProvider.INSTANCE,
      aupReminders, 
      TimeUnit.DAYS), 
      VOMSConfigurationConstants.MEMBERSHIP_CHECK_PERIOD, 
      300L);
    
    es.startBackgroundTask(new UpdateCATask(),
      VOMSConfigurationConstants.TRUST_ANCHORS_REFRESH_PERIOD);

    es.startBackgroundTask(new TaskStatusUpdater(), 30L);

    es.startBackgroundTask(new ExpiredRequestsPurgerTask(DAOFactory.instance(),
      EventManager.instance()),
      VOMSConfigurationConstants.VO_MEMBERSHIP_EXPIRED_REQ_PURGER_PERIOD, 300L);

    es.startBackgroundTask(new UserStatsTask(),
      VOMSConfigurationConstants.MONITORING_USER_STATS_UPDATE_PERIOD,
      UserStatsTask.DEFAULT_PERIOD_IN_SECONDS);

  }

  protected static void bootstrapAttributeAuthorityServices() {

    VOMSConfiguration conf = VOMSConfiguration.instance();

    try {
      org.opensaml.DefaultBootstrap.bootstrap();

    } catch (ConfigurationException e) {

      log.error("Error initializing OpenSAML:" + e.getMessage());

      if (log.isDebugEnabled())
        log.error("Error initializing OpenSAML:" + e.getMessage(), e);

      log.info("SAML endpoint will not be activated.");
      conf.setProperty(
        VOMSConfigurationConstants.VOMS_AA_SAML_ACTIVATE_ENDPOINT, false);
    }

    boolean x509AcEndpointEnabled = conf.getBoolean(
      VOMSConfigurationConstants.VOMS_AA_X509_ACTIVATE_ENDPOINT, false);

    if (x509AcEndpointEnabled) {

      log.info("Bootstrapping VOMS X.509 attribute authority.");
      ACGeneratorFactory.newACGenerator()
        .configure(conf.getServiceCredential());

      VOMSExecutorService es = VOMSExecutorService.instance();
      es.scheduleAtFixedRate(new PrintX509AAStatsTask(),
        PrintX509AAStatsTask.DEFAULT_PERIOD_IN_SECS,
        PrintX509AAStatsTask.DEFAULT_PERIOD_IN_SECS, TimeUnit.SECONDS);

    } else {
      log.info("X.509 attribute authority is disabled.");
    }

  }

  protected static void configureLogging(ServletContext ctxt) {

    String confDir = ctxt.getInitParameter("CONF_DIR");
    String vo = ctxt.getInitParameter("VO_NAME");

    String loggingConf = String.format("%s/%s/%s", confDir, vo, "logback.xml");

    File f = new File(loggingConf);

    if (!f.exists())
      throw new VOMSFatalException(String.format("Logging configuration "
        + "not found at path '%s'", loggingConf));

    if (!f.canRead())
      throw new VOMSFatalException(String.format("Logging configuration "
        + "is not readable: %s", loggingConf));

    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    JoranConfigurator configurator = new JoranConfigurator();

    configurator.setContext(lc);
    lc.reset();

    lc.putProperty(VOMSConfigurationConstants.VO_NAME, vo);

    try {
      configurator.doConfigure(f);

    } catch (JoranException e) {

      throw new VOMSFatalException("Error setting up the logging system", e);

    }

    StatusPrinter.printIfErrorsOccured(lc);
  }

  public static void bootstrapPersistence(VOMSConfiguration configuration) {

    Validate.notNull(configuration);
    HibernateFactory.initialize(configuration.getDatabaseProperties());

  }

  public static void start(ServletContext ctxt) {

    Thread
      .setDefaultUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());

    configureLogging(ctxt);

    VOMSConfiguration conf;

    try {

      conf = VOMSConfiguration.load(ctxt);

    } catch (VOMSConfigurationException e) {
      log.error("VOMS-Admin configuration failed!", e);
      throw new VOMSFatalException(e);
    }

    log.info("VOMS-Admin starting for VO: " + conf.getVOName());

    bootstrapPersistence(conf);

    checkDatabaseVersion();

    configureVelocity();

    configureEventManager();

    startBackgroundTasks();

    bootstrapAttributeAuthorityServices();

    PluginManager.instance().configurePlugins();

    ValidationManager.instance().startMembershipChecker();

    log.info("VOMS-Admin started succesfully.");
  }

  public static void stop() {

    VOMSExecutorService.shutdown();

    NotificationService.shutdown();

    HibernateFactory.shutdown();

    log.info("VOMS admin stopped .");
  }

}
