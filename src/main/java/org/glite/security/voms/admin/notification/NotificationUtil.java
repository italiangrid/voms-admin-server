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

package org.glite.security.voms.admin.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;

public class NotificationUtil {

	public static final Logger log = LoggerFactory.getLogger(NotificationUtil.class);

	public static List<String> getAdministratorsEmailList() {

		final String[] possibleBehaviours = { "admins", "service", "all" };

		String notificationBehaviour = VOMSConfiguration.instance().getString(
				VOMSConfigurationConstants.NOTIFICATION_NOTIFY_BEHAVIOUR, "admins");

		// Check user values for configuration behaviour, and if unknown value
		// is set, restore the default

		boolean notificationBehaviourValueOK = false;

		for (String b : possibleBehaviours)
			if (notificationBehaviour.trim().equals(b)) {
				notificationBehaviourValueOK = true;
				break;
			}

		if (!notificationBehaviourValueOK) {

			notificationBehaviour = "admins";
			log
					.warn("Unrecognized value for configuration option: "
							+ VOMSConfigurationConstants.NOTIFICATION_NOTIFY_BEHAVIOUR
							+ ". Possible values are: 'admins','service', 'all'. Setting the default value to 'admins'. Fix your configuration file!");
		}

		String serviceEmailAddress = VOMSConfiguration.instance().getString(
				VOMSConfigurationConstants.SERVICE_EMAIL_ADDRESS);

		ArrayList<String> adminEmails = new ArrayList<String>();

		VOMSContext voContext = VOMSContext.getVoContext();

		Set<VOMSAdmin> admins = voContext.getACL().getAdminsWithPermissions(
				VOMSPermission.getAllPermissions());

		if ("admins".equals(notificationBehaviour)
				|| "all".equals(notificationBehaviour)) {

			for (VOMSAdmin a : admins) {

				if (a.getEmailAddress() != null) {
					if (!adminEmails.contains(a.getEmailAddress()))
						adminEmails.add(a.getEmailAddress());
				}
			}
		}

		if ("service".equals(notificationBehaviour)
				|| "all".equals(notificationBehaviour))
			adminEmails.add(serviceEmailAddress);

		if (adminEmails.isEmpty()) {
			log
					.warn("No valid administrator email address found, falling back to service email address.");
			adminEmails.add(serviceEmailAddress);

		}

		return adminEmails;
	}

}
