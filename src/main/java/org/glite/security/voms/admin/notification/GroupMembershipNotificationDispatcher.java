package org.glite.security.voms.admin.notification;

import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.EventMask;
import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.registration.GroupMembershipApprovedEvent;
import org.glite.security.voms.admin.event.registration.GroupMembershipRejectedEvent;
import org.glite.security.voms.admin.event.registration.GroupMembershipRequestEvent;
import org.glite.security.voms.admin.event.registration.GroupMembershipSubmittedEvent;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;

public class GroupMembershipNotificationDispatcher extends BaseNotificationDispatcher{

	
	private static GroupMembershipNotificationDispatcher instance = null;
	
	public static GroupMembershipNotificationDispatcher instance(){
		
		if (instance == null)
			instance = new GroupMembershipNotificationDispatcher();
		
		return instance;
	}
	
	private GroupMembershipNotificationDispatcher() {
		super(new EventMask(EventType.GroupMembershipRequestEvent));
	}
											
	public void fire(Event event) {
		
		GroupMembershipRequestEvent e = (GroupMembershipRequestEvent) event;
		
		GroupMembershipRequest req = e.getRequest();
		
		if (e instanceof GroupMembershipSubmittedEvent){
			
			GroupMembershipSubmittedEvent ee = (GroupMembershipSubmittedEvent)e;
			
			HandleRequest msg = new HandleRequest(req,ee.getManagementURL());
					
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
}
