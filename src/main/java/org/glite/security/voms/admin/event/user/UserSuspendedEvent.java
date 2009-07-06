package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.VOMSUser.SuspensionReason;

public class UserSuspendedEvent extends UserMembershipEvent {

	SuspensionReason reason;
	
	public UserSuspendedEvent(VOMSUser user, SuspensionReason reason) {
		super(user);
		setReason(reason);
		
	}

	public SuspensionReason getReason() {
		return reason;
	}

	public void setReason(SuspensionReason reason) {
		this.reason = reason;
	}

	
	

	
}
