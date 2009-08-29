package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;

public class VOMembershipRequestConfirmedEvent extends VOMembershipRequestEvent {

	String url;

	public VOMembershipRequestConfirmedEvent(NewVOMembershipRequest r,
			String managementURL) {
		super(r);
		url = managementURL;
	}

	public String getUrl() {
		return url;
	}

}
