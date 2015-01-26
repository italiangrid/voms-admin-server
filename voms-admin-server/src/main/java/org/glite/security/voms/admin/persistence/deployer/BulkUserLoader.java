package org.glite.security.voms.admin.persistence.deployer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.util.SysconfigUtil;
import org.hibernate.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BulkUserLoader implements Runnable {

  public static final Logger log = LoggerFactory
    .getLogger(BulkUserLoader.class);

  final String vo;
  final String userFile;
  protected Dialect dialect;

  private void printUsageAndExit() {

    System.err.println("usage: BulkUserLoader <vo> <userfile>");
    System.exit(1);
  }

  private void logExceptionAndExit(Throwable t) {

    log.error(t.getMessage(), t);
    System.exit(1);
  }

  private void loadVOMSConfiguration() {

    System.setProperty(VOMSConfigurationConstants.VO_NAME, vo);
    VOMSConfiguration.load(null);
  }

  private String getVOConfigurationDir() {

    Properties sysconfProps = SysconfigUtil.loadSysconfig();

    String confDir = sysconfProps.getProperty(SysconfigUtil.SYSCONFIG_CONF_DIR);

    if (confDir == null)
      confDir = "/etc/voms-admin";

    return confDir;

  }

  private void checkVoExistence() {

    if (!isVoConfigured(vo)) {
      log.error("VO {} is not configured on this host.", vo);
      System.exit(1);
    }

  }

  private boolean isVoConfigured(String voName) {

    String confDir = getVOConfigurationDir();

    File voConfDir = new File(confDir + "/" + voName);

    if (voConfDir.exists() && voConfDir.isDirectory())
      return true;

    return false;

  }

  public BulkUserLoader(String[] args) {

    if (args.length != 2) {
      printUsageAndExit();
    }

    vo = args[0];
    userFile = args[1];

  }

  public static void main(String[] args) {

    new BulkUserLoader(args).run();

  }

  @Override
  public void run() {

    loadVOMSConfiguration();
    checkVoExistence();

    HibernateFactory.beginTransaction();

    VOMSUserDAO dao = VOMSUserDAO.instance();

    Scanner scanner = null;

    try {

      scanner = new Scanner(new File(userFile));

      while (scanner.hasNext()) {
        String line = scanner.nextLine();
        String[] tokens = line.split(",");

        VOMSUser u = new VOMSUser();
        u.setName(tokens[1]);
        u.setSurname(tokens[2]);
        u.setAddress("(undefined)");
        u.setEmailAddress(tokens[4]);
        u.setPhoneNumber(tokens[5]);
        u.setDn(String.format("/C=IT/O=IGI/CN=%s", tokens[3]));

        dao.create(u, "/C=IT/O=IGI/CN=Test CA");

        log.info("Created {}", u.getShortName());
        
      }

    } catch (FileNotFoundException e) {
      logExceptionAndExit(e);
    
    } finally {

      if (scanner != null) {
        scanner.close();
      }
    }

    HibernateFactory.commitTransaction();

  }

}
