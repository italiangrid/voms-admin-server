package org.glite.security.voms.admin.notification;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;

public class VOMSNotificationSettings extends BaseNotificationSettings {

  private VOMSNotificationSettings() {

  }

  public static VOMSNotificationSettings fromVOMSConfiguration() {

    VOMSNotificationSettings ns = new VOMSNotificationSettings();

    ns.sender = VOMSConfiguration.instance().getString(
      VOMSConfigurationConstants.SERVICE_EMAIL_ADDRESS);

    ns.smtpHost = VOMSConfiguration.instance().getString(
      VOMSConfigurationConstants.SERVICE_SMTP_SERVER);

    ns.smtpPort = VOMSConfiguration.instance().getInt(
      VOMSConfigurationConstants.SERVICE_SMTP_SERVER_PORT, 25);

    ns.username = VOMSConfiguration.instance().getString(
      VOMSConfigurationConstants.SERVICE_EMAIL_ACCOUNT_USERNAME, null);
    ns.password = VOMSConfiguration.instance().getString(
      VOMSConfigurationConstants.SERVICE_EMAIL_ACCOUNT_PASSWORD, null);

    ns.tls = VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.SERVICE_EMAIL_USE_TLS, false);

    ns.from = "VOMS Admin for VO " + VOMSConfiguration.instance().getVOName();

    return ns;
  }

}
