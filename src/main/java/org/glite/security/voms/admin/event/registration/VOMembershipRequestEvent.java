package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.event.EventType;
import org.glite.security.voms.admin.event.GenericEvent;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;

public abstract class VOMembershipRequestEvent extends GenericEvent {

	NewVOMembershipRequest request;

	public VOMembershipRequestEvent(NewVOMembershipRequest r) {
		super(EventType.VOMembershipRequestEvent);
		request = r;

	}

	public NewVOMembershipRequest getRequest() {
		return request;
	}

	public void setRequest(NewVOMembershipRequest request) {
		this.request = request;
	}

}
