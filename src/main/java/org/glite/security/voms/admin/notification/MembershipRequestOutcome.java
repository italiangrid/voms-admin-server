package org.glite.security.voms.admin.notification;

import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;

public class MembershipRequestOutcome extends AbstractVelocityNotification {

	Request request;
	
	public MembershipRequestOutcome(Request req) {
		this.request = req;
	}
	
	@Override
	protected void buildMessage() {
		
		String voName = VOMSConfiguration.instance().getVOName();
		
		if (request instanceof GroupMembershipRequest){
			
			GroupMembershipRequest groupReq = (GroupMembershipRequest)request;
			setSubject("Your membership request for group '"+groupReq.getGroupName()+"' has been "+groupReq.getStatus().toString());
			context.put("context", groupReq.getGroupName());
			
		}else if (request instanceof RoleMembershipRequest){
			RoleMembershipRequest roleReq = (RoleMembershipRequest)request;
			setSubject("Your membership request for role '"+roleReq.getFQAN()+"' has been "+roleReq.getStatus().toString());
			context.put("context", roleReq.getFQAN());
		}
		
		context.put("req", request);
		context.put("voName", voName);
		
		super.buildMessage();
	}
	
}
