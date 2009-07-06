package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.model.VOMSUser;

public class UserMembershipExpired extends UserMembershipEvent {

	public UserMembershipExpired(VOMSUser user) {
		super(user);
	}

}
