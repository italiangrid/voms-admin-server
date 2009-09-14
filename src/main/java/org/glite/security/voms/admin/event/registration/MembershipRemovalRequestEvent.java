package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.GenericEvent;
import org.glite.security.voms.admin.model.request.MembershipRemovalRequest;

public class MembershipRemovalRequestEvent extends GenericEvent {
	
	MembershipRemovalRequest request;
	
	public MembershipRemovalRequestEvent(MembershipRemovalRequest req){
		super(EventType.MembershipRemovalRequestEvent);
		this.request = req;
		
	}

	public MembershipRemovalRequest getRequest() {
		return request;
	}

	public void setRequest(MembershipRemovalRequest request) {
		this.request = request;
	}
	
}
