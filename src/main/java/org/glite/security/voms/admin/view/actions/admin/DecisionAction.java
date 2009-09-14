package org.glite.security.voms.admin.view.actions.admin;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.event.registration.GroupMembershipRequestEvent;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.operations.requests.ApproveGroupMembershipOperation;
import org.glite.security.voms.admin.operations.requests.ApproveMembershipRemovalRequestOperation;
import org.glite.security.voms.admin.operations.requests.ApproveRoleMembershipOperation;
import org.glite.security.voms.admin.operations.requests.ApproveVOMembershipOperation;
import org.glite.security.voms.admin.operations.requests.RejectGroupMembershipOperation;
import org.glite.security.voms.admin.operations.requests.RejectMembershipRemovalRequestOperation;
import org.glite.security.voms.admin.operations.requests.RejectRoleMembershipOperation;
import org.glite.security.voms.admin.operations.requests.RejectVOMembershipOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "pendingRequests.jsp"),
		@Result(name = BaseAction.INPUT, location = "pendingRequests.jsp") })
public class DecisionAction extends RequestActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String decision;

	@Override
	public String execute() throws Exception {

		if (request instanceof NewVOMembershipRequest) {

			if (decision.equals("approve"))
				ApproveVOMembershipOperation.instance(
						(NewVOMembershipRequest) request).execute();
			else
				RejectVOMembershipOperation.instance(
						(NewVOMembershipRequest) request).execute();
		}
		
		if (request instanceof GroupMembershipRequest){
			
			if (decision.equals("approve"))
				ApproveGroupMembershipOperation.instance((GroupMembershipRequest)request).execute();
			else
				RejectGroupMembershipOperation.instance((GroupMembershipRequest)request).execute();
		}
		
		if (request instanceof RoleMembershipRequest){
			
			if (decision.equals("approve"))
				ApproveRoleMembershipOperation.instance((RoleMembershipRequest)request).execute();
			else
				RejectRoleMembershipOperation.instance((RoleMembershipRequest)request).execute();
				
		}
		
		if (request instanceof MembershipRemovalRequest){
			if (decision.equals("approve"))
				ApproveMembershipRemovalRequestOperation.instance((MembershipRemovalRequest)request).execute();
			else
				RejectMembershipRemovalRequestOperation.instance((MembershipRemovalRequest)request).execute();
			
		}
		
		refreshPendingRequests();
		
		setDecision(null);
		return SUCCESS;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

}
