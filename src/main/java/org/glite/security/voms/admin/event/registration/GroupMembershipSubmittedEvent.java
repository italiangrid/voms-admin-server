package org.glite.security.voms.admin.event.registration;

import org.glite.security.voms.admin.model.request.GroupMembershipRequest;

public class GroupMembershipSubmittedEvent extends GroupMembershipRequestEvent {

	String managementURL;
	
	public GroupMembershipSubmittedEvent(GroupMembershipRequest req, String url) {
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
