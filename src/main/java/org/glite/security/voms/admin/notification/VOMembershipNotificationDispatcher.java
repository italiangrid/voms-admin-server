package org.glite.security.voms.admin.notification;

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
			HandleRequest msg = new HandleRequest(ee.getRequest(), ee.getUrl());

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
