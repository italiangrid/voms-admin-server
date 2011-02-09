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

import java.util.List;

import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventMask;
import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestApprovedEvent;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestConfirmedEvent;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestRejectedEvent;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestSubmittedEvent;
import org.glite.security.voms.admin.notification.messages.ConfirmRequest;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class VOMembershipNotificationDispatcher extends
		BaseNotificationDispatcher {

	private static VOMembershipNotificationDispatcher instance;
	
	public static VOMembershipNotificationDispatcher instance(){
		
		if (instance == null)
			instance = new VOMembershipNotificationDispatcher();
		
		return instance;
	}
	
	
	private VOMembershipNotificationDispatcher() {
		super(new EventMask(EventType.VOMembershipRequestEvent));
		
	}

	public void fire(Event e) {
		
		if (e instanceof VOMembershipRequestSubmittedEvent) {
			VOMembershipRequestSubmittedEvent ee = (VOMembershipRequestSubmittedEvent) e;

			String recipient = ee.getRequest().getRequesterInfo()
					.getEmailAddress();

			ConfirmRequest msg = new ConfirmRequest(recipient, ee
					.getConfirmURL(), ee.getCancelURL());
			NotificationService.instance().send(msg);

		} else if (e instanceof VOMembershipRequestConfirmedEvent) {

			VOMembershipRequestConfirmedEvent ee = (VOMembershipRequestConfirmedEvent) e;
			
			List<String> admins = NotificationUtil.getAdministratorsEmailList(VOMSContext.getVoContext(),
					VOMSPermission.getRequestsRWPermissions()); 
			
			HandleRequest msg = new HandleRequest(ee.getRequest(), 
					ee.getUrl(), admins );

			NotificationService.instance().send(msg);
		
		} else if (e instanceof VOMembershipRequestApprovedEvent) {

			RequestApproved msg = new RequestApproved(
					((VOMembershipRequestApprovedEvent) e).getRequest());

			NotificationService.instance().send(msg);
		
		} else if (e instanceof VOMembershipRequestRejectedEvent) {
			
			RequestRejected msg = new RequestRejected(((VOMembershipRequestRejectedEvent) e).getRequest(), null);
			NotificationService.instance().send(msg);
					
		}

	}

}
