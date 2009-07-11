package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.model.VOMSUser;

public class UserRestoredEvent extends UserMembershipEvent {

	public UserRestoredEvent(VOMSUser user) {
		super(user);
	}

}
