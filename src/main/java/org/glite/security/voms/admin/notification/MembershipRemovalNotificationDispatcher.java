package org.glite.security.voms.admin.notification;

import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventMask;
import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.registration.MembershipRemovalApprovedEvent;
import org.glite.security.voms.admin.event.registration.MembershipRemovalRejectedEvent;
import org.glite.security.voms.admin.event.registration.MembershipRemovalRequestEvent;
import org.glite.security.voms.admin.event.registration.MembershipRemovalSubmittedEvent;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;
import org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest;

public class MembershipRemovalNotificationDispatcher extends BaseNotificationDispatcher{

    private static transient MembershipRemovalNotificationDispatcher INSTANCE;
    
    public static MembershipRemovalNotificationDispatcher instance(){
	
	if (INSTANCE == null){
	    INSTANCE = new MembershipRemovalNotificationDispatcher();
	}
	
	return INSTANCE;
    }
    
    
    private MembershipRemovalNotificationDispatcher() {
	super(new EventMask(EventType.MembershipRemovalRequestEvent));
	
    }

    public void fire(Event e) {
	
	MembershipRemovalRequestEvent event = (MembershipRemovalRequestEvent) e;
	MembershipRemovalRequest req = event.getRequest();
	
	if (event instanceof MembershipRemovalSubmittedEvent){
	    
	    MembershipRemovalSubmittedEvent ee = (MembershipRemovalSubmittedEvent) event;
	    HandleRequest msg = new HandleRequest(req, ee.getManagementURL());
	    NotificationService.instance().send(msg);
	    
	    
	}else if (event instanceof MembershipRemovalApprovedEvent){
	    
	    RequestApproved msg =  new RequestApproved(((MembershipRemovalApprovedEvent)event).getRequest());
	    NotificationService.instance().send(msg);
	    
	}else if (event instanceof MembershipRemovalRejectedEvent){
	    
	    RequestRejected msg = new RequestRejected(((MembershipRemovalRejectedEvent)event).getRequest(),null);
	    NotificationService.instance().send(msg);
	}
	
    }
    
    

}
