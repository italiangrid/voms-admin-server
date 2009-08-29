package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.GenericEvent;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;

public abstract class UserMembershipEvent extends GenericEvent {

	VOMSUser user;

	VOMSGroup group;
	VOMSRole role;

	public UserMembershipEvent(VOMSUser user) {
		super(EventType.UserMembershipEvent);
		setUser(user);
	}

	public VOMSUser getUser() {
		return user;
	}

	public void setUser(VOMSUser user) {
		this.user = user;
	}

	public VOMSGroup getGroup() {
		return group;
	}

	public void setGroup(VOMSGroup group) {
		this.group = group;
	}

	public VOMSRole getRole() {
		return role;
	}

	public void setRole(VOMSRole role) {
		this.role = role;
	}

}
