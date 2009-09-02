package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.model.request.RoleMembershipRequest;

public class RoleMembershipSubmittedEvent extends RoleMembershipRequestEvent {

	String managementURL;
		
	public RoleMembershipSubmittedEvent(RoleMembershipRequest req, String url) {
		super(req);
		this.managementURL = url;
	}

	public String getManagementURL() {
		return managementURL;
	}

	public void setManagementURL(String managementURL) {
		this.managementURL = managementURL;
	}

}
