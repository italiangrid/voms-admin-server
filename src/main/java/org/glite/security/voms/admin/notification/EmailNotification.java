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
package org.glite.security.voms.admin.notification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.SimpleEmail;
import org.glite.security.voms.admin.common.PathNamingScheme;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.request.VOMSNotificationException;

public abstract class EmailNotification {

	private static final Log log = LogFactory.getLog(EmailNotification.class);

	List recipientList = new ArrayList();

	String subject;

	String message;

	int deliveryAttemptCount = 0;

	public EmailNotification() {

		super();
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {

		this.message = message;
	}

	public List getRecipientList() {

		return recipientList;
	}

	public void setRecipientList(List recipientList) {

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

	public void addRecipients(Collection c) {

		Iterator i = c.iterator();

		while (i.hasNext()) {
			addRecipient((String) i.next());
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
				VOMSConfiguration.SERVICE_EMAIL_ADDRESS);
		String smtpServer = VOMSConfiguration.instance().getString(
				VOMSConfiguration.SERVICE_SMTP_SERVER);
		int smtpServerPort = VOMSConfiguration.instance().getInt(
				VOMSConfiguration.SERVICE_SMTP_SERVER_PORT, 25);

		String userName = VOMSConfiguration.instance().getString(
				VOMSConfiguration.SERVICE_EMAIL_ACCOUNT_USERNAME, null);
		String userPassword = VOMSConfiguration.instance().getString(
				VOMSConfiguration.SERVICE_EMAIL_ACCOUNT_PASSWORD, null);
		boolean useTLS = VOMSConfiguration.instance().getBoolean(
				VOMSConfiguration.SERVICE_EMAIL_USE_TLS, false);

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
				throw new VOMSNotificationException("Empty reicipient list!");

			Iterator i = recipientList.iterator();
			while (i.hasNext())
				e.addTo((String) i.next());

			e.send();

		} catch (Throwable t) {
			log.error("Error setting up email notification!", t);
			throw new VOMSNotificationException(t.getMessage(), t);

		}
	}

	/**
	 * @return the deliveryAttemptCount
	 */
	public int getDeliveryAttemptCount() {

		return deliveryAttemptCount;
	}

	/**
	 * @param deliveryAttemptCount
	 *            the deliveryAttemptCount to set
	 */
	public void setDeliveryAttemptCount(int deliveryAttemptCount) {

		this.deliveryAttemptCount = deliveryAttemptCount;
	}

}
