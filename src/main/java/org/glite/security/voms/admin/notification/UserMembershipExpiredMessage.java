package org.glite.security.voms.admin.notification;

import org.glite.security.voms.admin.model.VOMSUser;

public class UserMembershipExpiredMessage extends VelocityEmailNotification {

	VOMSUser user;

	public UserMembershipExpiredMessage(VOMSUser u) {
		this.user = u;
	}

	@Override
	protected void buildMessage() {
		// TODO Auto-generated method stub

	}

}
