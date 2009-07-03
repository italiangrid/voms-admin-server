package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.GenericEvent;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.VOMSUser;

public class UserAUPEvent extends GenericEvent {

	VOMSUser user;
	AUP aup;
	
	public UserAUPEvent(VOMSUser user, AUP aup) {
		super(EventType.UserAUPEvent);
		setUser(user);
		setAup(aup);
		
	}

	public VOMSUser getUser() {
		return user;
	}

	public void setUser(VOMSUser user) {
		this.user = user;
	}

	public AUP getAup() {
		return aup;
	}

	public void setAup(AUP aup) {
		this.aup = aup;
	}
	
}
