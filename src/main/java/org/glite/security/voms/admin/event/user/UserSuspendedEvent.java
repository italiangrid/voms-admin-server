package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.model.VOMSUser;

public class UserSuspendedEvent extends UserMembershipEvent {

	String reason;
	
	public UserSuspendedEvent(VOMSUser user, String reason) {
		super(user);
		setReason(reason);
		
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	
}
