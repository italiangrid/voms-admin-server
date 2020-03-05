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
package org.glite.security.voms.admin.core;

import static org.glite.security.voms.admin.configuration.VOMSConfigurationConstants.EXPIRED_USER_CLEANUP_TASK_RUN_PERIOD;
import static org.glite.security.voms.admin.core.VOMSServiceConstants.DISABLE_BACKGROUND_TASK_PROPERTY;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

import org.apache.commons.lang.Validate;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.velocity.app.Velocity;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.configuration.VOMSConfigurationException;
import org.glite.security.voms.admin.core.tasks.CancelSignAUPTasksForExpiredUsersTask;
import org.glite.security.voms.admin.core.tasks.ExpiredRequestsPurgerTask;
import org.glite.security.voms.admin.core.tasks.PermissionCacheStatsLogger;
import org.glite.security.voms.admin.core.tasks.PrintX509AAStatsTask;
import org.glite.security.voms.admin.core.tasks.SignAUPReminderCheckTask;
import org.glite.security.voms.admin.core.tasks.SystemTimeProvider;
import org.glite.security.voms.admin.core.tasks.TaskStatusUpdater;
import org.glite.security.voms.admin.core.tasks.ThreadUncaughtExceptionHandler;
import org.glite.security.voms.admin.core.tasks.UpdateCATask;
import org.glite.security.voms.admin.core.tasks.UserStatsTask;
import org.glite.security.voms.admin.core.tasks.VOMSExecutorService;
import org.glite.security.voms.admin.core.tasks.user_cleanup.DefaultCleanupUserLookupStrategy;
import org.glite.security.voms.admin.core.tasks.user_cleanup.ExpiredUserCleanupTask;
import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.error.VOMSFatalException;
import org.glite.security.voms.admin.event.DebugEventLogListener;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.auditing.AuditLog;
import org.glite.security.voms.admin.event.permission_cache.AclEventsCleanPermissionCacheListener;
import org.glite.security.voms.admin.event.permission_cache.MembershipEventsCleanPermissionCacheListener;
import org.glite.security.voms.admin.event.permission_cache.UserEventsCleanPermissionCacheListener;
import org.glite.security.voms.admin.integration.PluginManager;
import org.glite.security.voms.admin.integration.cern.HrDbConfigurator;
import org.glite.security.voms.admin.integration.orgdb.OrgDBConfigurator;
import org.glite.security.voms.admin.integration.orgdb.servlet.OrgDbHibernateSessionFilter;
import org.glite.security.voms.admin.notification.NotificationServiceFactory;
import org.glite.security.voms.admin.notification.PersistentNotificationService;
import org.glite.security.voms.admin.notification.VOMSNotificationSettings;
import org.glite.security.voms.admin.notification.dispatchers.CertificateRequestsNotificationDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.DefaultNotificationDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.GroupMembershipNotificationDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.MembershipRemovalNotificationDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.RoleMembershipNotificationDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.SignAUPReminderDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.UserSuspendedDispatcher;
import org.glite.security.voms.admin.notification.dispatchers.VOMembershipNotificationDispatcher;
import org.glite.security.voms.admin.operations.DefaultPrincipalProvider;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.SchemaVersion;
import org.glite.security.voms.admin.persistence.dao.VOMSVersionDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.lookup.LookupPolicyProvider;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.util.validation.x509.VOMSAdminDnValidator;
import org.glite.security.voms.admin.view.util.DevModeEnabler;
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

  static final Logger LOG = LoggerFactory.getLogger(VOMSService.class);

  protected static void checkDatabaseVersion() {

    final String detectedDbVersion = VOMSVersionDAO.instance().getVersion().getAdminVersion();

    int detectedDbVersionInt = -1;

    try {
      detectedDbVersionInt = Math.abs(Integer.parseInt(detectedDbVersion));
    } catch (NumberFormatException ex) {
      String msg = String.format(
          "VOMS DATABASE SCHEMA ERROR: incompatible database. Found '%s' while expecting '%s'."
              + " Please upgrade the database for this installation using 'voms-db-util upgrade'"
              + " command.",
          detectedDbVersion, SchemaVersion.VOMS_ADMIN_DB_VERSION);

      LOG.error(msg);
      throw new VOMSFatalException(msg);
    }

    if (detectedDbVersionInt < SchemaVersion.VOMS_ADMIN_DB_VERSION_INT) {
      String msg = String.format(
          "VOMS DATABASE SCHEMA ERROR: incompatible database. Found '%s' while expecting '%s'."
              + " Please upgrade the database for this installation using 'voms-db-util upgrade'"
              + " command.",
          detectedDbVersion, SchemaVersion.VOMS_ADMIN_DB_VERSION);

      LOG.error(msg);
      throw new VOMSFatalException(msg);

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
      LOG.debug("Velocity setup ok!");

    } catch (Exception e) {

      LOG.error("Error initializing velocity template engine!");
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
    manager.register(SignAUPReminderDispatcher.instance());
    manager.register(AclEventsCleanPermissionCacheListener.instance());
    manager.register(UserEventsCleanPermissionCacheListener.instance());
    manager.register(MembershipEventsCleanPermissionCacheListener.instance());

  }

  protected static void startBackgroundTasks() {

    VOMSExecutorService es = VOMSExecutorService.instance();

    if (System.getProperty(DISABLE_BACKGROUND_TASK_PROPERTY) != null) {
      LOG.warn("Background tasks disabled, as requested by system property {}",
          DISABLE_BACKGROUND_TASK_PROPERTY);
      return;
    }


    VOMSConfiguration conf = VOMSConfiguration.instance();
    List<Integer> aupReminders = conf.getAUPReminderIntervals();

    es.startBackgroundTask(
        new SignAUPReminderCheckTask(DAOFactory.instance(), EventManager.instance(),
            SystemTimeProvider.INSTANCE, aupReminders, TimeUnit.DAYS),
        VOMSConfigurationConstants.MEMBERSHIP_CHECK_PERIOD, 300L);

    es.startBackgroundTask(
        new CancelSignAUPTasksForExpiredUsersTask(DAOFactory.instance(), EventManager.instance()),
        VOMSConfigurationConstants.MEMBERSHIP_CHECK_PERIOD, 300L);


    es.startBackgroundTask(new UpdateCATask(),
        VOMSConfigurationConstants.TRUST_ANCHORS_REFRESH_PERIOD);

    es.startBackgroundTask(new TaskStatusUpdater(), 30L);

    es.startBackgroundTask(
        new ExpiredRequestsPurgerTask(DAOFactory.instance(), EventManager.instance()),
        VOMSConfigurationConstants.VO_MEMBERSHIP_EXPIRED_REQ_PURGER_PERIOD, 300L);

    es.startBackgroundTask(new UserStatsTask(),
        VOMSConfigurationConstants.MONITORING_USER_STATS_UPDATE_PERIOD,
        UserStatsTask.DEFAULT_PERIOD_IN_SECONDS);

    es.scheduleAtFixedRate(new PermissionCacheStatsLogger(true), 1, 60, TimeUnit.SECONDS);

    ExpiredUserCleanupTask userCleanupTask =
        new ExpiredUserCleanupTask(new DefaultCleanupUserLookupStrategy(conf));

    es.startBackgroundTask(userCleanupTask, EXPIRED_USER_CLEANUP_TASK_RUN_PERIOD,
        TimeUnit.HOURS.toSeconds(4));

  }

  protected static void startNotificationService() {

    if (System.getProperty(DISABLE_BACKGROUND_TASK_PROPERTY) != null) {
      LOG.warn("Background tasks disabled, as requested by system property {}",
          DISABLE_BACKGROUND_TASK_PROPERTY);
      return;
    }

    if (!VOMSConfiguration.instance()
      .getBoolean(VOMSConfigurationConstants.NOTIFICATION_DISABLED, false)) {

      PersistentNotificationService ns = PersistentNotificationService.INSTANCE;

      ns.setNotificationSettings(VOMSNotificationSettings.fromVOMSConfiguration());

      ns.setDao(DAOFactory.instance().getNotificationDAO());

      ns.start();
    } else {
      LOG.warn("Notification service is DISABLED.");
    }

  }

  protected static void bootstrapAttributeAuthorityServices() {

    VOMSConfiguration conf = VOMSConfiguration.instance();

    try {
      org.opensaml.DefaultBootstrap.bootstrap();

    } catch (ConfigurationException e) {

      LOG.error("Error initializing OpenSAML:" + e.getMessage());

      if (LOG.isDebugEnabled())
        LOG.error("Error initializing OpenSAML:" + e.getMessage(), e);

      LOG.info("SAML endpoint will not be activated.");
      conf.setProperty(VOMSConfigurationConstants.VOMS_AA_SAML_ACTIVATE_ENDPOINT, false);
    }

    boolean x509AcEndpointEnabled =
        conf.getBoolean(VOMSConfigurationConstants.VOMS_AA_X509_ACTIVATE_ENDPOINT, false);

    if (x509AcEndpointEnabled) {

      LOG.info("Bootstrapping VOMS X.509 attribute authority.");
      ACGeneratorFactory.newACGenerator().configure(conf.getServiceCredential());

      VOMSExecutorService es = VOMSExecutorService.instance();
      es.scheduleAtFixedRate(new PrintX509AAStatsTask(),
          PrintX509AAStatsTask.DEFAULT_PERIOD_IN_SECS, PrintX509AAStatsTask.DEFAULT_PERIOD_IN_SECS,
          TimeUnit.SECONDS);

    } else {
      LOG.info("X.509 attribute authority is disabled.");
    }

  }

  protected static void configureLogging(ServletContext ctxt) {

    String confDir = ctxt.getInitParameter("CONF_DIR");
    String vo = ctxt.getInitParameter("VO_NAME");

    String loggingConf = String.format("%s/%s/%s", confDir, vo, "logback.xml");

    File f = new File(loggingConf);

    if (!f.exists())
      throw new VOMSFatalException(
          String.format("Logging configuration " + "not found at path '%s'", loggingConf));

    if (!f.canRead())
      throw new VOMSFatalException(
          String.format("Logging configuration " + "is not readable: %s", loggingConf));

    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    lc.setName(vo);

    JoranConfigurator configurator = new JoranConfigurator();

    configurator.setContext(lc);

    lc.reset();

    // We leave this here to avoid runtime errors for people that
    // update the service, but not the logging configuration
    // FIXME: to be removed at some point
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

  private static void initializeDnValidator() {

    VOMSAdminDnValidator.INSTANCE.initialize("/etc/grid-security/certificates", true);
  }

  private static void configureOrgDbHibernateSessionFitler(ServletContext ctxt) {
    if (!VOMSConfiguration.instance()
      .getRegistrationType()
      .equals(OrgDBConfigurator.ORGDB_REGISTRATION_TYPE)) {
      return;
    }

    FilterRegistration.Dynamic fr =
        ctxt.addFilter("orgdb-hibernate-session-filter", OrgDbHibernateSessionFilter.class);

    fr.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "*");

  }


  private static void setupGlobalApplicationObjects(ServletContext ctxt) {

    AUP aup = DAOFactory.instance().getAUPDAO().getVOAUP();

    ctxt.setAttribute("registrationEnabled", VOMSConfiguration.instance()
      .getBoolean(VOMSConfigurationConstants.REGISTRATION_SERVICE_ENABLED, true));

    ctxt.setAttribute("readOnlyPI", VOMSConfiguration.instance()
      .getBoolean(VOMSConfigurationConstants.VOMS_INTERNAL_RO_PERSONAL_INFORMATION, false));

    ctxt.setAttribute("readOnlyMembershipExpiration", VOMSConfiguration.instance()
      .getBoolean(VOMSConfigurationConstants.VOMS_INTERNAL_RO_MEMBERSHIP_EXPIRATION_DATE, false));

    ctxt.setAttribute("disableMembershipEndTime", VOMSConfiguration.instance()
      .getBoolean(VOMSConfigurationConstants.DISABLE_MEMBERSHIP_END_TIME, false));

    ctxt.setAttribute("defaultAUP", aup);

    ctxt.setAttribute("orgdbEnabled",
        VOMSConfiguration.instance()
          .getRegistrationType()
          .equals(OrgDBConfigurator.ORGDB_REGISTRATION_TYPE));

    ctxt.setAttribute("hrEnabled",
        VOMSConfiguration.instance()
          .getRegistrationType()
          .equals(HrDbConfigurator.HR_DB_REGISTRATION_TYPE));

  }


  public static void start(ServletContext ctxt) {

    Thread.setDefaultUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());

    configureLogging(ctxt);

    VOMSConfiguration conf;

    try {

      conf = VOMSConfiguration.load(ctxt);

    } catch (VOMSConfigurationException e) {
      LOG.error("VOMS-Admin configuration failed!", e);
      throw new VOMSFatalException(e);
    }

    LOG.info("VOMS-Admin starting for VO: " + conf.getVOName());

    bootstrapPersistence(conf);

    setupStrutsDevMode();

    checkDatabaseVersion();

    configureCertificateLookupPolicy(conf);

    configureVelocity();

    configureEventManager();

    startBackgroundTasks();

    startNotificationService();

    bootstrapAttributeAuthorityServices();

    PluginManager.instance().configurePlugins();

    ValidationManager.instance().startMembershipChecker();

    initializeDnValidator();

    configureOrgDbHibernateSessionFitler(ctxt);

    setupGlobalApplicationObjects(ctxt);

    LOG.info("VOMS-Admin started succesfully.");
  }

  private static void configureCertificateLookupPolicy(VOMSConfiguration conf) {

    boolean skipCaCheck = conf.getBoolean(VOMSConfigurationConstants.SKIP_CA_CHECK, false);

    if (skipCaCheck) {
      LOG.info(
          "CertificateLookupPolicy: VOMS Users, certificates and administrators will be looked up by certificate subject ({} == true)",
          VOMSConfigurationConstants.SKIP_CA_CHECK);
    } else {
      LOG.info(
          "CertficateLookupPolicy: VOMS Users, certificates and administrators will be looked up by certificate subject AND issuer ({} == false)",
          VOMSConfigurationConstants.SKIP_CA_CHECK);
    }

    LookupPolicyProvider.initialize(skipCaCheck);

  }

  private static void setupStrutsDevMode() {

    if (System.getProperty(VOMSServiceConstants.DEV_MODE_PROPERTY) != null) {
      Dispatcher.addDispatcherListener(new DevModeEnabler());
    }

  }

  public static void stop() {

    VOMSExecutorService.shutdown();

    NotificationServiceFactory.getNotificationService().shutdownNow();

    HibernateFactory.shutdown();

    LOG.info("VOMS admin stopped .");
  }

}
