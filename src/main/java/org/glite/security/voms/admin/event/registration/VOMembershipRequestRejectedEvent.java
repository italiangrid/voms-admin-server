package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;

public class VOMembershipRequestRejectedEvent extends VOMembershipRequestEvent {

	String reason;

	public VOMembershipRequestRejectedEvent(NewVOMembershipRequest r,
			String reason) {
		super(r);
		this.reason = reason;
	}

}
