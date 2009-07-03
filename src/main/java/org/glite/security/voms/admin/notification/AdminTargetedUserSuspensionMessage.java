package org.glite.security.voms.admin.notification;

import org.apache.velocity.VelocityContext;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.model.VOMSUser;

public class AdminTargetedUserSuspensionMessage extends
		VelocityEmailNotification {
	
	public static final String templateFileName = "AdminTargetedUserSuspensionMessage.vm";

	VOMSUser user;
	String suspensionReason;
	
	public AdminTargetedUserSuspensionMessage(VOMSUser u, String suspensionReason) {
		
		setTemplateFile(templateFileName);
		setUser(u);
		setSuspensionReason(suspensionReason);
		
	}
	
	
	@Override
	protected void buildMessage() {
		VOMSConfiguration conf = VOMSConfiguration.instance(); 
        String voName = conf.getVOName();
        
        setSubject(subjectPrefix+" User suspension notification");
        
        VelocityContext context = new VelocityContext();
        context.put( "voName", voName );
        context.put("user", getUser());
        context.put( "recipient", getRecipientList().get(0));
        context.put("suspensionReason", suspensionReason);
        
        buildMessageFromTemplate(context);
        

	}


	public VOMSUser getUser() {
		return user;
	}


	public void setUser(VOMSUser user) {
		this.user = user;
	}


	public String getSuspensionReason() {
		return suspensionReason;
	}


	public void setSuspensionReason(String suspensionReason) {
		this.suspensionReason = suspensionReason;
	}
	
	

}
