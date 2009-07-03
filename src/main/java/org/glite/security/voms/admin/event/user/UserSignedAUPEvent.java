package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.VOMSUser;

public class UserSignedAUPEvent extends UserAUPEvent {

	public UserSignedAUPEvent(VOMSUser user, AUP aup) {
		super(user, aup);
		// TODO Auto-generated constructor stub
	}

}
