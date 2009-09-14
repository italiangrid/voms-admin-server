package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.model.request.MembershipRemovalRequest;

public class MembershipRemovalSubmittedEvent extends
		MembershipRemovalRequestEvent {

	String managementURL;
	
	public MembershipRemovalSubmittedEvent(MembershipRemovalRequest req, String managementURL) {
		super(req);
		this.managementURL = managementURL;
	}

	public String getManagementURL() {
		return managementURL;
	}

	public void setManagementURL(String managementURL) {
		this.managementURL = managementURL;
	}

}
