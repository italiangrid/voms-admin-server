package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.GenericEvent;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;

public class RoleMembershipRequestEvent extends GenericEvent {

	RoleMembershipRequest request;
	
	public RoleMembershipRequestEvent(RoleMembershipRequest req) {
		super(EventType.RoleMembershipRequestEvent);
		this.request = req;
	}

	public RoleMembershipRequest getRequest() {
		return request;
	}

	public void setRequest(RoleMembershipRequest request) {
		this.request = request;
	}

	
}
