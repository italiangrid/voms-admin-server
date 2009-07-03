package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.model.VOMSUser;

public class UserDeletedEvent extends UserMembershipEvent {

	public UserDeletedEvent(VOMSUser user) {
		super(user);
	}

}
