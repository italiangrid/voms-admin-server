package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.GenericEvent;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;

public class GroupMembershipRequestEvent extends GenericEvent {

	GroupMembershipRequest request;
	
	public GroupMembershipRequestEvent(GroupMembershipRequest req) {
		super(EventType.GroupMembershipRequestEvent);
		request = req;
	}

	public GroupMembershipRequest getRequest() {
		return request;
	}

	public void setRequest(GroupMembershipRequest request) {
		this.request = request;
	}	
}
