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
