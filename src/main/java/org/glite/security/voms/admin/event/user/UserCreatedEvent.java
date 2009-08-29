package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.model.VOMSUser;

public class UserCreatedEvent extends UserMembershipEvent {

	public UserCreatedEvent(VOMSUser user) {
		super(user);
		// TODO Auto-generated constructor stub
	}

}
