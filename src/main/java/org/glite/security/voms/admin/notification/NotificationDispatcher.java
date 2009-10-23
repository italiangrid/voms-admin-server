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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventListener;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.EventMask;
import org.glite.security.voms.admin.event.registration.GroupMembershipApprovedEvent;
import org.glite.security.voms.admin.event.registration.GroupMembershipRejectedEvent;
import org.glite.security.voms.admin.event.registration.GroupMembershipRequestEvent;
import org.glite.security.voms.admin.event.registration.GroupMembershipSubmittedEvent;
import org.glite.security.voms.admin.event.registration.RoleMembershipApprovedEvent;
import org.glite.security.voms.admin.event.registration.RoleMembershipRejectedEvent;
import org.glite.security.voms.admin.event.registration.RoleMembershipRequestEvent;
import org.glite.security.voms.admin.event.registration.RoleMembershipSubmittedEvent;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestApprovedEvent;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestConfirmedEvent;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestRejectedEvent;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestSubmittedEvent;
import org.glite.security.voms.admin.event.user.SignAUPTaskAssignedEvent;
import org.glite.security.voms.admin.event.user.UserMembershipExpired;
import org.glite.security.voms.admin.event.user.UserSuspendedEvent;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.notification.messages.AdminTargetedUserSuspensionMessage;
import org.glite.security.voms.admin.notification.messages.ConfirmRequest;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;
import org.glite.security.voms.admin.notification.messages.SignAUPMessage;
import org.glite.security.voms.admin.notification.messages.UserMembershipExpiredMessage;
import org.glite.security.voms.admin.notification.messages.UserTargetedUserSuspensionMessage;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class NotificationDispatcher implements EventListener {

	public static final Log log = LogFactory
			.getLog(NotificationDispatcher.class);

	private static NotificationDispatcher instance = null;

	private NotificationDispatcher() {
		EventManager.instance().register(this);
	}

	public static final NotificationDispatcher instance() {
		if (instance == null)
			instance = new NotificationDispatcher();

		return instance;

	}

	public void fire(Event e) {

		if (e instanceof UserSuspendedEvent) {

			handle((UserSuspendedEvent) e);

		} else if (e instanceof SignAUPTaskAssignedEvent) {

			handle((SignAUPTaskAssignedEvent) e);

		} else if (e instanceof UserMembershipExpired) {

			UserMembershipExpiredMessage msg = new UserMembershipExpiredMessage(
					((UserMembershipExpired) e).getUser());
			msg.addRecipients(getAdministratorsEmailList());
			NotificationService.instance().send(msg);

			// Also inform user she has been suspended ?
		} else if (e instanceof VOMembershipRequestSubmittedEvent) {
			VOMembershipRequestSubmittedEvent ee = (VOMembershipRequestSubmittedEvent) e;

			String recipient = ee.getRequest().getRequesterInfo()
					.getEmailAddress();

			ConfirmRequest msg = new ConfirmRequest(recipient, ee
					.getConfirmURL(), ee.getCancelURL());
			NotificationService.instance().send(msg);

		} else if (e instanceof VOMembershipRequestConfirmedEvent) {

			VOMembershipRequestConfirmedEvent ee = (VOMembershipRequestConfirmedEvent) e;
			HandleRequest msg = new HandleRequest(ee.getRequest(), ee.getUrl());

			msg.addRecipients(getAdministratorsEmailList());

			NotificationService.instance().send(msg);
		
		} else if (e instanceof VOMembershipRequestApprovedEvent) {

			RequestApproved msg = new RequestApproved(
					((VOMembershipRequestApprovedEvent) e).getRequest());

			NotificationService.instance().send(msg);
		
		} else if (e instanceof VOMembershipRequestRejectedEvent) {
			
			RequestRejected msg = new RequestRejected(((VOMembershipRequestRejectedEvent) e).getRequest(), null);
			NotificationService.instance().send(msg);
			
		}	else if (e instanceof GroupMembershipRequestEvent){
			
			handle((GroupMembershipRequestEvent)e);		
			
		} else if (e instanceof RoleMembershipRequestEvent){
			
			handle((RoleMembershipRequestEvent)e);
		}

	}

	public EventMask getMask() {
		return null;
	}
	
	
	protected void handle(RoleMembershipRequestEvent e){
		
		RoleMembershipRequest req = e.getRequest();
		
		if (e instanceof RoleMembershipSubmittedEvent){
			
			HandleRequest msg = new HandleRequest(req, ((RoleMembershipSubmittedEvent) e).getManagementURL());
			msg.addRecipients(getAdministratorsEmailList());
			
			NotificationService.instance().send(msg);
			
		}
		
		if (e instanceof RoleMembershipApprovedEvent){
			
			NotificationService.instance().send(new RequestApproved(req));
			
		}
		
		if (e instanceof RoleMembershipRejectedEvent){
			
			NotificationService.instance().send(new RequestRejected(req, null));
			
		}
		
	}
	protected void handle(GroupMembershipRequestEvent e){
		
		GroupMembershipRequest req = e.getRequest();
		
		if (e instanceof GroupMembershipSubmittedEvent){
			
			GroupMembershipSubmittedEvent ee = (GroupMembershipSubmittedEvent)e;
			
			HandleRequest msg = new HandleRequest(req,ee.getManagementURL());
			
			msg.addRecipients(getAdministratorsEmailList());
			
			NotificationService.instance().send(msg);
			
		}
		
		if (e instanceof GroupMembershipApprovedEvent){
			
			RequestApproved msg = new RequestApproved(req);
			NotificationService.instance().send(msg);
		
		}
		
		if (e instanceof GroupMembershipRejectedEvent){
		
			RequestRejected msg = new RequestRejected(req, null);
			
			NotificationService.instance().send(msg);
		}
		
	}

	protected void handle(UserSuspendedEvent e) {

		// Notify admins
		AdminTargetedUserSuspensionMessage msg = new AdminTargetedUserSuspensionMessage(
				e.getUser(), e.getReason().getMessage());
		
		UserTargetedUserSuspensionMessage usrMsg = new UserTargetedUserSuspensionMessage(e.getUser(),
				e.getReason().getMessage());
		
		msg.addRecipients(getAdministratorsEmailList());
		usrMsg.addRecipient(e.getUser().getEmailAddress());
		
		NotificationService.instance().send(msg);
		NotificationService.instance().send(usrMsg);

	}

	protected void handle(SignAUPTaskAssignedEvent e) {

		SignAUPMessage msg = new SignAUPMessage(e.getUser(), e.getAup());
		msg.addRecipient(e.getUser().getEmailAddress());
		NotificationService.instance().send(msg);

	}

	
	protected List<String> getAdministratorsEmailList() {
		
		final String[] possibleBehaviours = {"admins","service","all"};
		
		String notificationBehaviour = VOMSConfiguration.instance().getString(VOMSConfiguration.NOTIFICATION_NOTIFY_BEHAVIOUR,"admins");
		
		
		// Check user values for configuration behaviour, and if unknown value is set, restore the default
		
		boolean notificationBehaviourValueOK = false;
		
		for (String b: possibleBehaviours)
			if (notificationBehaviour.trim().equals(b)){
				notificationBehaviourValueOK = true;
				break;
			}
			
		if (!notificationBehaviourValueOK){
			
			notificationBehaviour = "admins";
			log.warn("Unrecognized value for configuration option: "+VOMSConfiguration.NOTIFICATION_NOTIFY_BEHAVIOUR+
				". Possible values are: 'admins','service', 'all'. Setting the default value to 'admins'. Fix your configuration file!");
		}
		
		
		String serviceEmailAddress = VOMSConfiguration.instance().getString(
				VOMSConfiguration.SERVICE_EMAIL_ADDRESS);
		
		ArrayList<String> adminEmails = new ArrayList<String>();
		
		VOMSContext voContext = VOMSContext.getVoContext();
		
		Set<VOMSAdmin> admins = voContext.getACL().getAdminsWithPermissions(
				VOMSPermission.getAllPermissions());
		
		if ("admins".equals(notificationBehaviour) || "all".equals(notificationBehaviour)){
			
			for (VOMSAdmin a : admins) {
				
				if (a.getEmailAddress() != null){
					if (!adminEmails.contains(a.getEmailAddress()))
						adminEmails.add(a.getEmailAddress());
				}
			}			
		}
		
		
		if ("service".equals(notificationBehaviour) || "all".equals(notificationBehaviour))	
			adminEmails.add(serviceEmailAddress);
		
		
		if (adminEmails.isEmpty()){
			log.warn("No valid administrator email address found, falling back to service email address.");
			adminEmails.add(serviceEmailAddress);
			
		}

		return adminEmails;
	}

}
