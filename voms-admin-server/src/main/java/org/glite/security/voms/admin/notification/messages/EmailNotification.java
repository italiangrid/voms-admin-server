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
package org.glite.security.voms.admin.notification.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.mail.SimpleEmail;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.notification.VOMSNotificationException;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.util.PathNamingScheme;

public abstract class EmailNotification implements VOMSNotification {

  private static final Logger log = LoggerFactory
    .getLogger(EmailNotification.class);

  private List<String> recipientList = new ArrayList<String>();

  private String subject;

  private String message;

  private int deliveryAttemptCount = 0;

  public EmailNotification() {

    super();
  }

  public String getMessage() {

    return message;
  }

  public void setMessage(String message) {

    this.message = message;
  }

  public List<String> getRecipientList() {

    return recipientList;
  }

  public void setRecipientList(List<String> recipientList) {

    this.recipientList = recipientList;
  }

  public String getSubject() {

    return subject;
  }

  public void setSubject(String subject) {

    this.subject = subject;
  }

  public void addRecipient(String recipientAddress) {

    if ((recipientAddress != null) && (!recipientAddress.trim().equals("")))
      recipientList.add(recipientAddress);
  }

  public void addRecipients(Collection<String> c) {

    Iterator<String> i = c.iterator();

    while (i.hasNext()) {
      addRecipient(i.next());
    }
  }

  public void addAdminToRecipients(VOMSAdmin a) {

    log.debug("addAdminToRecipients::: Admin dn:" + a.getDn());
    if (!a.isInternalAdmin())
      addRecipient(a.getEmailAddress());

    else {

      if (a.isGroupAdmin()) {
        log.debug("addAdminToRecipients::: admin is group admin.");
        addGroupMembersToRecipients(a.getDn());
      } else if (a.isRoleAdmin())
        log.debug("addAdminToRecipients::: admin is role admin.");

      String groupName = PathNamingScheme.getGroupName(a.getDn());
      String roleName = PathNamingScheme.getRoleName(a.getDn());

      log.debug("GroupName: " + groupName + " RoleName:" + roleName);

      addRoleMembersToRecipients(groupName, roleName);
    }

  }

  public void addGroupMembersToRecipients(String groupName) {

    VOMSGroup g = VOMSGroupDAO.instance().findByName(groupName);
    addRecipients(g.getMembersEmailAddresses());

  }

  public void addRoleMembersToRecipients(String groupName, String roleName) {

    VOMSGroup g = VOMSGroupDAO.instance().findByName(groupName);
    VOMSRole r = VOMSRoleDAO.instance().findByName(roleName);

    if (r == null || g == null)
      return;

    addRecipients(r.getMembersEmailAddresses(g));
  }

  protected abstract void buildMessage();

  public void send() {

    if (message == null)
      buildMessage();

    SimpleEmail e = new SimpleEmail();

    String sender = VOMSConfiguration.instance().getString(
      VOMSConfigurationConstants.SERVICE_EMAIL_ADDRESS);
    String smtpServer = VOMSConfiguration.instance().getString(
      VOMSConfigurationConstants.SERVICE_SMTP_SERVER);
    int smtpServerPort = VOMSConfiguration.instance().getInt(
      VOMSConfigurationConstants.SERVICE_SMTP_SERVER_PORT, 25);

    String userName = VOMSConfiguration.instance().getString(
      VOMSConfigurationConstants.SERVICE_EMAIL_ACCOUNT_USERNAME, null);
    String userPassword = VOMSConfiguration.instance().getString(
      VOMSConfigurationConstants.SERVICE_EMAIL_ACCOUNT_PASSWORD, null);
    boolean useTLS = VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.SERVICE_EMAIL_USE_TLS, false);

    deliveryAttemptCount++;
    try {

      e.setHostName(smtpServer);
      e.setSmtpPort(smtpServerPort);
      e.setFrom(sender, "VOMS Admin for VO "
        + VOMSConfiguration.instance().getVOName());
      e.setSubject(subject);
      e.setMsg(message);

      if (userName != null && userPassword != null)
        e.setAuthentication(userName, userPassword);

      e.setTLS(useTLS);

      if (recipientList.isEmpty())
        throw new VOMSNotificationException("Empty recipient list!");

      Iterator<String> i = recipientList.iterator();
      while (i.hasNext())
        e.addTo(i.next());

      e.send();

    } catch (Throwable t) {
      log.error("Error setting up email notification!", t);
      throw new VOMSNotificationException(t.getMessage(), t);

    }
  }

  @Override
  /**
   * @return the deliveryAttemptCount
   */
  public int getDeliveryAttemptCount() {

    return deliveryAttemptCount;
  }

}
