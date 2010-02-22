package org.glite.security.voms.admin.notification;

import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventMask;
import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.registration.CertificateRequestApprovedEvent;
import org.glite.security.voms.admin.event.registration.CertificateRequestRejectedEvent;
import org.glite.security.voms.admin.event.registration.CertificateRequestSubmittedEvent;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;

public class CertificateRequestsNotificationDispatcher extends
		BaseNotificationDispatcher {

	private static CertificateRequestsNotificationDispatcher INSTANCE;
	
	public static CertificateRequestsNotificationDispatcher instance(){
		
		if (INSTANCE == null)
			INSTANCE = new CertificateRequestsNotificationDispatcher();
		
		return INSTANCE;
	}
	
	private CertificateRequestsNotificationDispatcher() {
		super(new EventMask(EventType.CertificateRequestEvent));
	}

	public void fire(Event e) {
		
		if (e instanceof CertificateRequestSubmittedEvent){
			
			CertificateRequestSubmittedEvent ee = (CertificateRequestSubmittedEvent)e;
			
			HandleRequest msg = new HandleRequest(ee.getRequest(), ee.getManagementURL());
			
			NotificationService.instance().send(msg);
		}
		
		if (e instanceof CertificateRequestApprovedEvent){
			
			RequestApproved msg = new RequestApproved(((CertificateRequestApprovedEvent)e).getRequest());
			NotificationService.instance().send(msg);
		}
		
		if (e instanceof CertificateRequestRejectedEvent){
			RequestRejected msg = new RequestRejected(((CertificateRequestRejectedEvent)e).getRequest(), null);
			NotificationService.instance().send(msg);
		}

	}

}
