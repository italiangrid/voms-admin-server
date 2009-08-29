package org.glite.security.voms.admin.task;

import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.VOMSUser;

public class AUPTaskEventContext {

	VOMSUser user;
	AUP aup;

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

	public AUPTaskEventContext(VOMSUser u, AUP a) {
		setUser(u);
		setAup(a);
	}
}
