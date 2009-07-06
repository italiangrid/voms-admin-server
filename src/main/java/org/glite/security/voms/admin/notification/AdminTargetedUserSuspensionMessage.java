package org.glite.security.voms.admin.notification;

import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.model.VOMSUser;

public class AdminTargetedUserSuspensionMessage extends
		AbstractVelocityNotification {
	
	VOMSUser user;
	String suspensionReason;
	
	public AdminTargetedUserSuspensionMessage(VOMSUser u, String suspensionReason) {
		
		this.user = u;
		this.suspensionReason = suspensionReason;
		
		
	}
	
	@Override
	protected void buildMessage() {

		VOMSConfiguration conf = VOMSConfiguration.instance(); 
        String voName = conf.getVOName();
        
        setSubject(subjectPrefix+" User suspension notification");
        context.put( "voName", voName );
        context.put("user", user);
        context.put( "recipient", getRecipientList().get(0));
        context.put("suspensionReason", suspensionReason);
		super.buildMessage();
	}
	
	
}
