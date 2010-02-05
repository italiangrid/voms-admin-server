package org.glite.security.voms.admin.notification;

import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventMask;
import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.registration.RoleMembershipApprovedEvent;
import org.glite.security.voms.admin.event.registration.RoleMembershipRejectedEvent;
import org.glite.security.voms.admin.event.registration.RoleMembershipRequestEvent;
import org.glite.security.voms.admin.event.registration.RoleMembershipSubmittedEvent;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;

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
			
			HandleRequest msg = new HandleRequest(req, ((RoleMembershipSubmittedEvent) e).getManagementURL());
			
			
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
