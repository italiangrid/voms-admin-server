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
import org.glite.security.voms.admin.event.registration.RoleMembershipApprovedEvent;
import org.glite.security.voms.admin.event.registration.RoleMembershipRejectedEvent;
import org.glite.security.voms.admin.event.registration.RoleMembershipRequestEvent;
import org.glite.security.voms.admin.event.registration.RoleMembershipSubmittedEvent;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest;

public class RoleMembershipNotificationDispatcher extends BaseNotificationDispatcher{

	
	private static RoleMembershipNotificationDispatcher instance;
	
	public static RoleMembershipNotificationDispatcher instance(){
		
		if (instance ==  null)
			instance = new RoleMembershipNotificationDispatcher();
		
		return instance;
	}
	
	private RoleMembershipNotificationDispatcher() {
		super(new EventMask(EventType.RoleMembershipRequestEvent));
	}

	public void fire(Event event) {
		
		RoleMembershipRequestEvent e = (RoleMembershipRequestEvent)event;
		
		RoleMembershipRequest req = e.getRequest();
		
		if (e instanceof RoleMembershipSubmittedEvent){
			
			RoleMembershipSubmittedEvent ee = (RoleMembershipSubmittedEvent)e;
			
			VOMSContext context = VOMSContext.instance(ee.getRequest().getFQAN());
			List<String> admins = NotificationUtil.getAdministratorsEmailList(context, VOMSPermission.getRequestsRWPermissions());
			
			
			HandleRequest msg = new HandleRequest(req, ee.getManagementURL(), admins);
			
			
			NotificationService.instance().send(msg);
			
		}
		
		if (e instanceof RoleMembershipApprovedEvent){
			
			NotificationService.instance().send(new RequestApproved(req));
			
		}
		
		if (e instanceof RoleMembershipRejectedEvent){
			
			NotificationService.instance().send(new RequestRejected(req, null));
			
		}

	}

}
