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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides utilities for the VOMS Admin notification service
 * 
 * 
 * @author andreaceccanti
 *
 */
public class NotificationUtil {

    public static final Logger log = LoggerFactory
	    .getLogger(NotificationUtil.class);

    /**
     * This method resolves the email address for a given VOMS administrator. For "normal" administrators, the email address stored in the database
     * is returned (if present). For VOMS group and role administrators, the group/role members email addresses are returned.
     * The resulting email address collection contains no duplicates.
     * 
     *    
     * @param a
     * A {@link VOMSAdmin} administrator  
     * @return
     * A possibly empty list of email addresses associated with the {@link VOMSAdmin} administrator 
     * 
     */
    public static Collection<String> resolveAdministratorEmailAddress(VOMSAdmin a){
	
	HashSet<String> emails = new HashSet<String>();
	
	if (!a.isInternalAdmin()) {

	    if (a.getEmailAddress() != null
		    && !"".equals(a.getEmailAddress().trim()))
		emails.add(a.getEmailAddress().trim());

	} else {

	    if (a.isGroupAdmin()) {

		VOMSGroup g = VOMSGroupDAO.instance().findByName(
			a.getDn());
		
		emails.addAll(g.getMembersEmailAddresses());

	    } else if (a.isRoleAdmin()) {

		VOMSRole r = VOMSRoleDAO.instance().findByName(
			PathNamingScheme.getRoleName(a.getDn()));
		
		VOMSGroup g = VOMSGroupDAO.instance().findByName(
			PathNamingScheme.getGroupName(a.getDn()));
		
		emails.addAll(r.getMembersEmailAddresses(g));

	    }

	}
	
	return emails;
    }
    
    
    private static String getNotificationBehaviour() {

	final String[] possibleBehaviours = { "admins", "service", "all" };

	String notificationBehaviour = VOMSConfiguration.instance().getString(
		VOMSConfigurationConstants.NOTIFICATION_NOTIFY_BEHAVIOUR,
		"admins");

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
	    log.warn("Unrecognized value for configuration option: "
		    + VOMSConfigurationConstants.NOTIFICATION_NOTIFY_BEHAVIOUR
		    + ". Possible values are: 'admins','service', 'all'. Setting the default value to 'admins'. Fix your configuration file!");
	}

	return notificationBehaviour;

    }

    /**
     * Returns the email addresses of the VOMS Administrators having ALL permissions in the VO root
     * group.
     * 
     * @return
     * A list of administrator email addresses having the given permissions in the specified context.
     */
    public static List<String> getAdministratorsEmailList() {

	return getAdministratorsEmailList(VOMSContext.getVoContext(),
		VOMSPermission.getAllPermissions());
    }

    /**
     * Returns the email addresses of the VOMS Administrators with a given permission in 
     * a specific VOMS context. 
     * 
     * @param context
     * The {@link VOMSContext}, i.e., a group or a qualified role.
     * 
     * @param permission
     * The requested {@link VOMSPermission}.
     * 
     * @return
     * A list of administrator email addresses having the given permissions in the specified context.  
     */
    public static List<String> getAdministratorsEmailList(VOMSContext context,
	    VOMSPermission permission) {

	if (context == null)
	    throw new IllegalArgumentException(
		    "Please provide a non-null context!");

	if (permission == null)
	    throw new IllegalArgumentException(
		    "Please provide a non-null permission!");

	String notificationBehaviour = getNotificationBehaviour();

	String serviceEmailAddress = VOMSConfiguration.instance().getString(
		VOMSConfigurationConstants.SERVICE_EMAIL_ADDRESS);

	HashSet<String> adminEmails = new HashSet<String>();

	Set<VOMSAdmin> admins = context.getACL().getAdminsWithPermissions(
		permission);

	if ("admins".equals(notificationBehaviour)
		|| "all".equals(notificationBehaviour)) {

	    for (VOMSAdmin a : admins) {
		
		adminEmails.addAll(resolveAdministratorEmailAddress(a));
		
	    }

	    if ("service".equals(notificationBehaviour)
		    || "all".equals(notificationBehaviour)) {

		adminEmails.add(serviceEmailAddress);
	    }

	    if (adminEmails.isEmpty()) {
		log.warn("No valid administrator email address found, falling back to service email address.");
		adminEmails.add(serviceEmailAddress);

	    }

	}

	ArrayList<String> emailList = new ArrayList<String>();
	emailList.addAll(adminEmails);
	
	return emailList;

    }
}