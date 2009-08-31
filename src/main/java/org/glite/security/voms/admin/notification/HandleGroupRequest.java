package org.glite.security.voms.admin.notification;

import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;

public class HandleGroupRequest extends AbstractVelocityNotification {

	GroupMembershipRequest request;
	String managementURL;
	
	HandleGroupRequest(GroupMembershipRequest gmReq, String manageURL){
		this.request = gmReq;
		this.managementURL = manageURL;	
	}
	
	@Override
	protected void buildMessage() {
		
		String voName = VOMSConfiguration.instance().getVOName();
		setSubject("A GROUP membership request for VO " + voName
				+ " requires your approval.");
		
		context.put("voName", voName);
		context.put("recipient", "VO Admin");
		context.put("req", request);
		context.put("managementURL", managementURL);
		
		super.buildMessage();
	}
	
}
