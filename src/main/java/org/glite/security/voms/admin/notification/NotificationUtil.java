package org.glite.security.voms.admin.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class NotificationUtil {

	public static final Log log = LogFactory.getLog(NotificationUtil.class);

	public static List<String> getAdministratorsEmailList() {

		final String[] possibleBehaviours = { "admins", "service", "all" };

		String notificationBehaviour = VOMSConfiguration.instance().getString(
				VOMSConfiguration.NOTIFICATION_NOTIFY_BEHAVIOUR, "admins");

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
							+ VOMSConfiguration.NOTIFICATION_NOTIFY_BEHAVIOUR
							+ ". Possible values are: 'admins','service', 'all'. Setting the default value to 'admins'. Fix your configuration file!");
		}

		String serviceEmailAddress = VOMSConfiguration.instance().getString(
				VOMSConfiguration.SERVICE_EMAIL_ADDRESS);

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
