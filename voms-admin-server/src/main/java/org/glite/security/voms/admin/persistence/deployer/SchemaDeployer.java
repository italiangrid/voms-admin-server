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
package org.glite.security.voms.admin.persistence.deployer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.ConfigurationException;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.core.tasks.DatabaseSetupTask;
import org.glite.security.voms.admin.core.tasks.UpdateCATask;
import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.event.auditing.AuditLogHelper;
import org.glite.security.voms.admin.event.vo.acl.ACLUpdatedEvent;
import org.glite.security.voms.admin.event.vo.admin.AdminCreatedEvent;
import org.glite.security.voms.admin.event.vo.admin.AdminDeletedEvent;
import org.glite.security.voms.admin.operations.CurrentAdminPrincipal;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.DBUtil;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.SchemaVersion;
import org.glite.security.voms.admin.persistence.dao.ACLDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSVersionDAO;
import org.glite.security.voms.admin.persistence.error.VOMSDatabaseException;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSDBVersion;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.util.SysconfigUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.type.LongType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("deprecation")
public class SchemaDeployer {

  public static final String ORACLE_PRODUCT_NAME = "Oracle";
  public static final String MYSQL_PRODUCT_NAME = "MySQL";

  private static final Logger log = LoggerFactory
    .getLogger(SchemaDeployer.class);

  protected CommandLineParser parser = new PosixParser();

  protected HelpFormatter helpFormatter = new HelpFormatter();

  protected Options options;

  String command;

  String vo;

  String hibernateConfigFile = null;

  String hibernatePropertiesFile = null;

  String adminDN = null;

  String adminCA = null;

  String adminEmailAddress = null;

  SessionFactory sf;

  Configuration hibernateConfiguration = null;

  Dialect dialect;

  AuditLogHelper auditLogHelper = new AuditLogHelper(
    CurrentAdminPrincipal.LOCAL_DB_PRINCIPAL);

  public SchemaDeployer(String[] args) {

    setupCLParser();
    checkArguments(args);
    execute();

    HibernateFactory.shutdown();

  }

  private void printUpgradeScript() {

    SchemaUpdate updater = new SchemaUpdate(loadHibernateConfiguration());
    updater.execute(true, false);

  }

  private void checkDatabaseConnectivity() {

    log.info("Checking database connectivity...");

    Session s = null;

    try {

      s = HibernateFactory.getFactory().openSession();
      s.beginTransaction();

    } catch (GenericJDBCException e) {
      log.error("");

      log
        .error("===========================================================================================================================");
      log
        .error("Error connecting to the voms database! Check your database settings and ensure that the database backend is up and running.");
      log
        .error("============================================================================================================================");

      if (log.isDebugEnabled())
        log.error(e.getMessage(), e);

      System.exit(-1);

    } finally {

      if (s != null)
        s.close();
    }

    log.info("Database contacted succesfully");
  }

  private void checkDatabaseWritable() {

    log.info("Checking that the database is writable...");

    Session s = null;

    try {

      s = HibernateFactory.getFactory().openSession();
      Transaction t = s.beginTransaction();

      s.createSQLQuery("create table writetest(integer a)");
      t.commit();

      t = s.beginTransaction();
      s.createSQLQuery("drop table writetest");
      t.commit();

    } catch (Throwable t) {

      log
        .error("Error writing to the voms database. Check your database settings and that the database backend is up and running.");

      if (log.isDebugEnabled())
        log
          .error(
            "Error opening connection to the voms database. Check your database settings, or ensure that the local is up & running\nCause:"
              + t.getMessage(), t);

      throw new VOMSDatabaseException(
        "Error opening connection to the voms database. Check your database settings, or ensure that the local is up & running",
        t);

    } finally {

      if (s != null)
        s.close();
    }

    log.info("Database is writable.");

  }

  private void execute() {

    System.setProperty(VOMSConfigurationConstants.VO_NAME, vo);
    VOMSConfiguration.load(null);

    hibernateConfiguration = loadHibernateConfiguration();
    HibernateFactory.initialize(hibernateConfiguration);

    if (command.equals("deploy"))
      doDeploy();
    else if (command.equals("undeploy"))
      doUndeploy();
    else if (command.equals("upgrade"))
      doUpgrade();
    else if (command.equals("add-admin"))
      doAddAdmin();
    else if (command.equals("remove-admin"))
      doRemoveAdmin();
    else if (command.equals("upgrade-script"))
      printUpgradeScript();
    else if (command.equals("check-connectivity")) {
      checkDatabaseExistence();
    } else if (command.equals("grant-read-only-access")) {
      doGrantROAccess();
    } else {

      System.err.println("Unkown command specified: " + command);
      System.exit(2);
    }

  }

  private boolean isOracleBackend() {

    Session s = HibernateFactory.getSession();
    s.beginTransaction();

    DatabaseMetaData dbMetadata = null;
    String dbProductName = null;

    try {

      dbMetadata = s.connection().getMetaData();
      dbProductName = dbMetadata.getDatabaseProductName();

    } catch (HibernateException e) {

      log
        .error(
          "Hibernate error accessing database metadata from Hibernate connection!",
          e);
      System.exit(-1);

    } catch (SQLException e) {

      log
        .error(
          "SQL error while accessing database metadata from Hibernate connection!",
          e);
      System.exit(-1);

    }

    log.debug("Detected database: " + dbProductName);
    return dbProductName.trim().equals(ORACLE_PRODUCT_NAME);

  }

  private void printException(Throwable t) {

    if (t.getMessage() != null)
      log.error(t.getMessage());
    else
      log.error(t.toString());

    if (log.isDebugEnabled()) {

      if (t.getMessage() != null)
        log.error(t.getMessage(), t);
      else
        log.error(t.toString(), t);

    }
  }

  private void printAllException(Throwable t) {

    if (t != null)
      printException(t);

    if (t.getCause() != null) {
      log.error("caused by:");
      printAllException(t.getCause());

    }
  }

  private void printExceptions(List l) {

    Iterator i = l.iterator();

    while (i.hasNext()) {

      Throwable t = (Throwable) i.next();
      log.error(t.getMessage());

      if (log.isDebugEnabled())
        printAllException(t);
    }

  }

  private String getVarcharType() {

    if (isOracleBackend())
      return "varchar2";
    else
      return "varchar";
  }

  private String getTimestampType() {

    if (isOracleBackend())
      return "timestamp";
    else
      return "datetime";

  }

  private ResultSet getTableNamesMatchingPattern(DatabaseMetaData md,
    String pattern) {

    String[] names = { "TABLE" };

    ResultSet tableNames = null;

    try {

      tableNames = md.getTables(null, "%", pattern, names);

    } catch (SQLException e) {
      log.error("Error reading table names from database metadata object!", e);
      System.exit(-1);
    }

    return tableNames;
  }

  private int checkDatabaseExistence() {

    checkVoExistence();
    checkDatabaseConnectivity();
    log.info("Checking database existence...");
    Session s = HibernateFactory.getSession();

    DatabaseMetaData dbMetadata = null;

    try {

      dbMetadata = s.connection().getMetaData();

    } catch (HibernateException e) {

      log
        .error(
          "Hibernate error accessing database metadata from Hibernate connection!",
          e);
      System.exit(-1);

    } catch (SQLException e) {

      log
        .error(
          "SQL error while accessing database metadata from Hibernate connection!",
          e);
      System.exit(-1);

    }

    ResultSet tableNames = getTableNamesMatchingPattern(dbMetadata, "%");

    boolean foundACL2 = false;
    boolean foundACL = false;
    boolean foundAUP = false;

    try {

      while (tableNames.next()) {
        String tName = tableNames.getString("TABLE_NAME");
        if (tName.equals("ACL") || tName.equals("acl"))
          foundACL = true;

        if (tName.equals("ACL2") || tName.equals("acl2"))
          foundACL2 = true;

        if (tName.equalsIgnoreCase("aup"))
          foundAUP = true;
      }

      HibernateFactory.commitTransaction();

    } catch (SQLException e) {

      log.error("Error accessing table names result set!", e);
      System.exit(-1);

    } catch (HibernateException e) {
      log.error("Error committing read-only hibernate transaction!", e);
      System.exit(-1);

    }

    if (foundACL2 && foundAUP) {
      log.info("Found existing voms-admin > 2.5.x database...");
      return 3;
    }
    if (foundACL2) {
      log.info("Found existing voms-admin 2.0.x database...");
      return 2;
    }

    if (foundACL) {
      log.info("Found existing voms-admin 1.2.x database...");
      return 1;

    }

    log.info("No voms-admin database found.");
    return -1;
  }

  private List<String> parseUpgradeScript(String filename) throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(this
      .getClass().getResourceAsStream(filename)));

    ArrayList<String> commands = new ArrayList<String>();

    String line;

    do {
      line = reader.readLine();
      if (line != null)
        commands.add(line);

    } while (line != null);

    return commands;

  }

  private List<String> loadUpgradeScriptToV4() throws IOException {

    String upgradeScriptFileName = "/upgrade-scripts/V4_new-versioning.sql";
    return parseUpgradeScript(upgradeScriptFileName);
  }

  private boolean doUpgradeToV4() {

    int existingDB = checkDatabaseExistence();

    if (existingDB < 0) {
      log.error("No voms-admin database found to upgrade!");
      System.exit(-1);
    }

    if (existingDB < 3) {
      log.error("Upgrade not supported from this database version.");
      System.exit(-1);
    }

    if (existingDB == 3) {

      String[] versionsToBeUpgraded = { "3.2.0", "3.3.0", "3.3.1", "3.3.2", "3.3.3" };
      String adminVersion = VOMSVersionDAO.instance().getVersion()
        .getAdminVersion().trim();

      if (adminVersion.equals(SchemaVersion.VOMS_ADMIN_DB_VERSION)) {
        log.warn("No upgrade needed, found schema version {}", adminVersion);
        return false;
      }

      boolean upgradeSupported = false;

      for (String v : versionsToBeUpgraded) {
        if (adminVersion.equals(v)) {
          log
            .debug(
              "Found VOMS Admin database version {} that requires a schema upgrade.",
              adminVersion);
          upgradeSupported = true;
          break;
        }
      }

      if (!upgradeSupported) {

        log.error("Upgrade not supported from schema version {}", adminVersion);
        return false;
      }

      log.info("Upgrading database schema from version {} to version {}",
        adminVersion, SchemaVersion.VOMS_ADMIN_DB_VERSION);

      try {

        List<String> upgradeScript = loadUpgradeScriptToV4();

        ArrayList<Exception> exceptions = new ArrayList<Exception>();
        
        HibernateFactory.beginTransaction();
        Statement statement = HibernateFactory.getSession().connection()
          .createStatement();

        for (String command : upgradeScript) {
          try {
            log.info(command);
            statement.executeUpdate(command);

          } catch (SQLException e) {
            log.error("Error while executing: " + command);
            exceptions.add(e);
          }
        }

        if (!exceptions.isEmpty()) {
          log.error("Error upgrading voms database!");
          printExceptions(exceptions);
          HibernateFactory.rollbackTransaction();
          System.exit(2);
        }

        // Update database version
        VOMSVersionDAO.instance().setupVersion();
        HibernateFactory.commitTransaction();
        return true;

      } catch (Exception e) {
        log.error("Error upgrading VOMS database to latest schema: {}",
          e.getMessage(), e);
        HibernateFactory.rollbackTransaction();
      }
    }

    return false;
  }

  private void doUpgrade() {

    checkVoExistence();

    int existingDB = checkDatabaseExistence();

    if (existingDB == -1) {
      log.error("No voms-admin 1.2.x database found to upgrade!");
      System.exit(-1);
    }

    if (existingDB == 1) {
      log.error("Upgrading voms-admin 1.2.x database is not supported! ");
      System.exit(-1);
    }

    if (existingDB == 2) {
      log.info("Upgrading voms-admin 2.0.x database is not supported!");
      System.exit(-1);
    }

    if (existingDB == 3) {

      boolean upgradePerformed = doUpgradeToV4();

      if (upgradePerformed)
        log.info("The upgrade procedure has changed the VOMS database.");
      else
        log.info("The upgrade procedure has NOT changed the VOMS database.");
    }
  }

  private void doRemoveAdmin() {

    checkVoExistence();

    if (adminDN == null || adminCA == null)
      throw new VOMSException("adminDN or adminCA is not set!");

    try {

      VOMSAdmin a = VOMSAdminDAO.instance().getByName(adminDN, adminCA);

      if (a == null) {

        log.info("Admin '" + adminDN + "," + adminCA
          + "' does not exists in database...");
        return;
      }

      List<ACL> affectedACLs = ACLDAO.instance().deletePermissionsForAdmin(a);

      VOMSAdminDAO.instance().delete(a);

      for (ACL acl : affectedACLs) {
        auditLogHelper.saveAuditEvent(ACLUpdatedEvent.class, acl);
      }

      auditLogHelper.saveAuditEvent(AdminDeletedEvent.class, a);

      HibernateFactory.commitTransaction();

      log.info("Administrator '{},{}' removed", new String[] { a.getDn(),
        a.getCa().getSubjectString() });

    } catch (Throwable t) {

      log.error("Error removing administrator!");
      log.error(t.toString(), t);

      System.exit(-1);
    }
  }

  private void doAddAdmin() {

    checkVoExistence();
    if (adminDN == null || adminCA == null)
      throw new VOMSException("adminDN or adminCA not set!");

    HibernateFactory.beginTransaction();

    try {

      VOMSAdmin a = VOMSAdminDAO.instance().getByName(adminDN, adminCA);

      if (a != null) {

        log.info("Admin '" + a.getDn() + "," + a.getCa().getDn()
          + "' already exists in database...");
        log
          .warn("This admin will be granted full privileges on the VOMS database.");
      } else {

        log.info("Admin '" + adminDN + "," + adminCA
          + "' not found. It will be created...");
        // Admin does not exist, create it!
        a = VOMSAdminDAO.instance().create(adminDN, adminCA, adminEmailAddress);
        auditLogHelper.saveAuditEvent(AdminCreatedEvent.class, a);

      }

      Iterator<VOMSGroup> i = VOMSGroupDAO.instance().findAll().iterator();

      while (i.hasNext()) {

        VOMSGroup g = i.next();
        g.getACL().setPermissions(a, VOMSPermission.getAllPermissions());
        log
          .info("Adding ALL permissions on '{}' for admin '{},{}'",
            new String[] { g.toString(), a.getDn(),
              a.getCa().getSubjectString() });

        Iterator<VOMSRole> rolesIter = VOMSRoleDAO.instance().findAll()
          .iterator();

        while (rolesIter.hasNext()) {

          VOMSRole r = rolesIter.next();
          r.getACL(g).setPermissions(a, VOMSPermission.getAllPermissions());
          log.info("Adding ALL permissions on role '{}/{}' for admin '{},{}'",
            new String[] { g.toString(), r.toString(), a.getDn(),
              a.getCa().getSubjectString() });

          HibernateFactory.getSession().save(r);
          auditLogHelper.saveAuditEvent(ACLUpdatedEvent.class, r.getACL(g));
        }

        HibernateFactory.getSession().save(g);
        auditLogHelper.saveAuditEvent(ACLUpdatedEvent.class, g.getACL());

      }

      HibernateFactory.commitTransaction();

    } catch (Throwable t) {

      log.error("Error adding new administrator!");
      log.error(t.toString(), t);

      System.exit(-1);
    }

  }

  private String getVOConfigurationDir() {

    Properties sysconfProps = SysconfigUtil.loadSysconfig();

    String confDir = sysconfProps.getProperty(SysconfigUtil.SYSCONFIG_CONF_DIR);

    if (confDir == null)
      confDir = "/etc/voms-admin";

    return confDir;

  }

  private boolean isVoConfigured(String voName) {

    String confDir = getVOConfigurationDir();

    File voConfDir = new File(confDir + "/" + voName);

    if (voConfDir.exists() && voConfDir.isDirectory())
      return true;

    return false;

  }

  private Configuration loadHibernateConfiguration() {

    Configuration cfg;

    if (hibernatePropertiesFile == null) {

      cfg = DBUtil.loadHibernateConfiguration(getVOConfigurationDir(), vo);

    } else {

      cfg = DBUtil.loadHibernateConfiguration(hibernatePropertiesFile);
    }

    dialect = Dialect.getDialect(cfg.getProperties());

    cfg.configure();

    return cfg;
  }

  private void doUndeploy() {

    checkVoExistence();

    log.info("Undeploying voms database...");

    int existingDB = checkDatabaseExistence();

    if (existingDB == 1) {
      log
        .error("This tool cannot undeploy voms-admin 1.2.x database! Please upgrade to voms-admin 2 or use voms-admin-configure 1.2.x tools to undeploy this database.");
      System.exit(-1);
    }

    if (existingDB == 2) {

      log
        .error("This tool cannot undeploy voms-admin 2.0.x databases! Please either upgrade the database to voms-admin 2.5 (using this tool) or use voms-admin-configure 2.0.x"
          + " tools to undeploy this database");

      System.exit(-1);
    }
    if (existingDB < 0) {

      log.error("No voms-admin database found!");
      System.exit(-1);
    }

    checkDatabaseWritable();

    SchemaExport export = new SchemaExport(hibernateConfiguration);

    export.drop(false, true);

    List l = export.getExceptions();

    if (!l.isEmpty()) {
      log.error("Error undeploying voms database!");
      printExceptions(l);
      System.exit(2);
    }

    log.info("Database undeployed correctly!");

  }

  private void checkVoExistence() {

    if (!isVoConfigured(vo)) {
      log.error("VO {} is not configured on this host.", vo);
      System.exit(1);
    }

  }

  private void doGrantROAccess() {

    try {

      checkVoExistence();

      checkDatabaseWritable();

      HibernateFactory.beginTransaction();

      VOMSAdmin anyUserAdmin = VOMSAdminDAO.instance()
        .getAnyAuthenticatedUserAdmin();

      VOMSPermission readOnlyPerms = VOMSPermission.getEmptyPermissions()
        .setContainerReadPermission().setMembershipReadPermission();

      List<VOMSGroup> groups = VOMSGroupDAO.instance().findAll();

      for (VOMSGroup g : groups) {

        g.getACL().setPermissions(anyUserAdmin, readOnlyPerms);

        log.info(
          "Granting read-only access to any authenticated user on group '{}'",
          g.getName());

        List<VOMSRole> roles = VOMSRoleDAO.instance().findAll();

        for (VOMSRole r : roles) {

          r.getACL(g).setPermissions(anyUserAdmin, readOnlyPerms);
          log
            .info(
              "Granting read-only access to any authenticated user on role '{}/{}'",
              new String[] { g.toString(), r.toString() });

          HibernateFactory.getSession().save(r);
          auditLogHelper.saveAuditEvent(ACLUpdatedEvent.class, r.getACL(g));

        }

        HibernateFactory.getSession().save(g);
        auditLogHelper.saveAuditEvent(ACLUpdatedEvent.class, g.getACL());
      }

      HibernateFactory.commitTransaction();

    } catch (Throwable t) {

      log.error("Error creating read-only access grants!");
      log.error(t.toString(), t);

      System.exit(-1);
    }

  }

  private void doDeploy() {

    checkVoExistence();

    int existingDb = checkDatabaseExistence();

    if (existingDb > 0) {
      log
        .warn("Existing voms database found. Will not overwrite the database!");
      System.exit(0);
    }

    checkDatabaseWritable();

    SchemaExport exporter = new SchemaExport(hibernateConfiguration);

    exporter.execute(true, true, false, true);

    log.info("Deploying voms database...");

    List l = exporter.getExceptions();

    if (!l.isEmpty()) {
      log.error("Error deploying voms database!");
      printExceptions(l);
      System.exit(2);

    }

    // This is needed as the version of hibernate we are using
    // does not support defining indexes on join table columns
    // See: https://hibernate.atlassian.net/browse/HHH-4263
    CreateAuditEventDataIndexes createIndexTask = new CreateAuditEventDataIndexes(
      HibernateFactory.getSession());

    createIndexTask.run();

    UpdateCATask caTask = new UpdateCATask();
    caTask.run();

    DatabaseSetupTask task = DatabaseSetupTask.instance();
    task.run();

    HibernateFactory.commitTransaction();
    log.info("Database deployed correctly!");

  }

  protected void setupCLParser() {

    options = new Options();

    options.addOption(OptionBuilder.withLongOpt("help")
      .withDescription("Displays helps and exits.").create("h"));

    options
      .addOption(OptionBuilder
        .withLongOpt("command")
        .withDescription(
          "Specifies the command to be executed: deploy,undeploy,upgrade,add-admin")
        .hasArg().create("command"));

    options.addOption(OptionBuilder.withLongOpt("vo")
      .withDescription("Specifies the vo name.").hasArg().create("vo"));

    options.addOption(OptionBuilder.withLongOpt("config")
      .withDescription("Specifies the hibernate config file to be used.")
      .hasArg().create("config"));

    options.addOption(OptionBuilder.withLongOpt("properties")
      .withDescription("Specifies the hibernate properties file to be used.")
      .hasArg().create("properties"));

    options
      .addOption(OptionBuilder
        .withLongOpt("dn")
        .withDescription(
          "Specifies the dn for the admin to add (valid only if add-admin command is given).")
        .hasArg().create("dn"));

    options
      .addOption(OptionBuilder
        .withLongOpt("ca")
        .withDescription(
          "Specifies the ca for the admin to add (valid only if add-admin command is given).")
        .hasArg().create("ca"));

    options
      .addOption(OptionBuilder
        .withLongOpt("email")
        .withDescription(
          "Specifies the email address for the admin to add (valid only if add-admin command is given).")
        .hasArg().create("email"));

  }

  protected void checkArguments(String[] args) {

    try {

      CommandLine line = parser.parse(options, args);

      if (line.hasOption("h"))

        printHelpMessageAndExit(0);

      if (!line.hasOption("command")) {

        System.err.println("No command specified!");
        printHelpMessageAndExit(1);

      }

      if (!line.hasOption("vo")) {
        System.err.println("No vo specified!");
        printHelpMessageAndExit(1);
      }

      command = line.getOptionValue("command");

      // FIXME: use an Enumeration for the commands!!
      if (!command.equals("deploy") && !command.equals("upgrade")
        && !command.equals("add-admin") && !command.equals("remove-admin")
        && !command.equals("undeploy") && !command.equals("upgrade-script")
        && !command.equals("check-connectivity")
        && !command.equals("grant-read-only-access")) {

        System.err.println("Unknown command specified: " + command);
        printHelpMessageAndExit(2);
      }

      vo = line.getOptionValue("vo");

      if (line.hasOption("config"))
        hibernateConfigFile = line.getOptionValue("config");

      if (line.hasOption("properties"))
        hibernatePropertiesFile = line.getOptionValue("properties");

      if (line.hasOption("dn"))
        adminDN = line.getOptionValue("dn");

      if (line.hasOption("ca"))
        adminCA = line.getOptionValue("ca");

      if (line.hasOption("email"))
        adminEmailAddress = line.getOptionValue("email");

    } catch (ParseException e) {

      throw new VOMSException("Error parsing command-line arguments: "
        + e.getMessage(), e);

    }

  }

  private void printHelpMessageAndExit(int exitStatus) {

    helpFormatter.printHelp("SchemaDeployer", options);
    System.exit(exitStatus);

  }

  public static void main(String[] args) throws ConfigurationException {

    new SchemaDeployer(args);

  }

}
