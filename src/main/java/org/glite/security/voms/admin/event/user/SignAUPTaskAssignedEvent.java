package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.VOMSUser;

public class SignAUPTaskAssignedEvent extends UserAUPEvent{

	public SignAUPTaskAssignedEvent(VOMSUser user, AUP aup) {
		super(user, aup);
		
	}

}
