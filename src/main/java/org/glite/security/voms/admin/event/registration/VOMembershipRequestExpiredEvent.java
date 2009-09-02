package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;

public class VOMembershipRequestExpiredEvent extends VOMembershipRequestEvent {

	public VOMembershipRequestExpiredEvent(NewVOMembershipRequest r) {
		super(r);
	}

}
