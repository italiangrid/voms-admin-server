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
import org.glite.security.voms.admin.event.registration.VOMembershipRequestApprovedEvent;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestConfirmedEvent;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestSubmittedEvent;
import org.glite.security.voms.admin.event.user.SignAUPTaskAssignedEvent;
import org.glite.security.voms.admin.event.user.UserMembershipExpired;
import org.glite.security.voms.admin.event.user.UserSuspendedEvent;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSMapping;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class ServiceNotificationDispatcher implements EventListener {

	public static final Log log  = LogFactory.getLog(ServiceNotificationDispatcher.class);
	
	private static ServiceNotificationDispatcher instance = null;
	
	private ServiceNotificationDispatcher() {
		EventManager.instance().register(this);
	}
	
	
	public static final ServiceNotificationDispatcher instance(){
		if (instance == null)
			instance = new ServiceNotificationDispatcher();
		
		return instance;
		
	}
	
	
	public void fire(Event e) {
		
		if (e instanceof UserSuspendedEvent){
			
			handle((UserSuspendedEvent)e);
			
		}else if (e instanceof SignAUPTaskAssignedEvent){
			
			handle((SignAUPTaskAssignedEvent)e);
			
		}else if (e instanceof UserMembershipExpired){
			
			UserMembershipExpiredMessage msg = new UserMembershipExpiredMessage(((UserMembershipExpired) e).getUser());
			msg.addRecipients(getVoAdminEmailList());
			NotificationService.instance().send(msg);
			
			// Also inform user she has been suspended ?
		}else if (e instanceof VOMembershipRequestSubmittedEvent){
			VOMembershipRequestSubmittedEvent ee = (VOMembershipRequestSubmittedEvent)e;
			
			String recipient = ee.getRequest().getRequesterInfo().getEmailAddress();
			
			ConfirmRequest msg = new ConfirmRequest(recipient,ee.getConfirmURL(), ee.getCancelURL());
			NotificationService.instance().send(msg);		
						
		}else if (e instanceof VOMembershipRequestConfirmedEvent){
			
			VOMembershipRequestConfirmedEvent ee = (VOMembershipRequestConfirmedEvent)e;
			HandleRequest msg = new HandleRequest(ee.getRequest(),ee.getUrl());
			
			msg.addRecipients(getVoAdminEmailList());
			
			NotificationService.instance().send(msg);
		}else if (e instanceof VOMembershipRequestApprovedEvent){
			
			RequestApproved msg = new RequestApproved(((VOMembershipRequestApprovedEvent) e).getRequest().getRequesterInfo().getEmailAddress());
			
			NotificationService.instance().send(msg);
		}
		
	}

	public EventMask getMask() {
		return null;
	}

	protected void handle(UserSuspendedEvent e){
		
		// Notify admins
		AdminTargetedUserSuspensionMessage msg = new AdminTargetedUserSuspensionMessage(e.getUser(), e.getReason().getMessage());
		msg.addRecipients(getVoAdminEmailList());
		NotificationService.instance().send(msg);
		
	}
	
	protected void handle(SignAUPTaskAssignedEvent e){
		
		SignAUPMessage msg = new SignAUPMessage(e.getUser(), e.getAup());
		msg.addRecipient(e.getUser().getEmailAddress());
		NotificationService.instance().send(msg);
		
	}
	
	protected List<String> getVoAdminEmailList(){
		
		
		
		ArrayList<String> adminEmails = new ArrayList<String>();
		VOMSContext voContext = VOMSContext.getVoContext();
        Set<VOMSAdmin> admins = voContext.getACL().getAdminsWithPermissions(VOMSPermission.getAllPermissions());
        
       
        for (VOMSAdmin a: admins){
        	
        	if (a.getEmailAddress() != null)
        		adminEmails.add(a.getEmailAddress());
        }
        
        String serviceEmailAddress = VOMSConfiguration.instance().getString(
                VOMSConfiguration.SERVICE_EMAIL_ADDRESS );
        
        if (adminEmails.isEmpty())
        	adminEmails.add(serviceEmailAddress);
        
        return adminEmails;
	}        	
        
}
