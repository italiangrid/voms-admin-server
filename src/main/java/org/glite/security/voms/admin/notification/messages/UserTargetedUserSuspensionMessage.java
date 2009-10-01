package org.glite.security.voms.admin.notification.messages;

import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.model.VOMSUser;

public class UserTargetedUserSuspensionMessage extends
		AbstractVelocityNotification {

	VOMSUser user;
	String suspensionReason;
	
	public UserTargetedUserSuspensionMessage(VOMSUser u, String suspensionReason) {
		
		this.user = u;
		this.suspensionReason = suspensionReason;

	}
	
	@Override
	protected void buildMessage() {

		VOMSConfiguration conf = VOMSConfiguration.instance();
		String voName = conf.getVOName();

		setSubject("Membership suspension notification");
		context.put("voName", voName);
		context.put("recipient", getRecipientList().get(0));
		context.put("suspensionReason", suspensionReason);
		super.buildMessage();
	}

}
