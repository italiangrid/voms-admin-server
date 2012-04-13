package org.glite.security.voms.admin.view.actions.admin;

import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.requests.DECISION;
import org.glite.security.voms.admin.operations.requests.HandleVOMembershipRequest;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "pendingRequests.jsp"),
		@Result(name = BaseAction.INPUT, location = "pendingRequests.jsp"),
		@Result(name = TokenInterceptor.INVALID_TOKEN_CODE, location ="pendingRequests.jsp")
})
		
@InterceptorRef(value = "authenticatedStack", params = {
		"token.includeMethods", "execute" })
public class MembershipDecisionAction extends DecisionAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void validateVOMembershipRequest() {

		
		NewVOMembershipRequest r = (NewVOMembershipRequest) getModel();

		VOMSUser sameDnUser = (VOMSUser) FindUserOperation.instance(
				r.getRequesterInfo().getCertificateSubject(),
				r.getRequesterInfo().getCertificateIssuer()).execute();

		if (sameDnUser != null && decision.equals("approve")){
			addActionError("A user with such certificate already exists. Please reject such request.");
			decision = null;
		}
	
	}
	
	protected void stripFalseFromApprovedGroups(){
		
		if (approvedGroups != null)
			while (approvedGroups.contains("false"))
				approvedGroups.remove("false");
	}
	
	@Override
	public void validate() {
		
		if (request instanceof NewVOMembershipRequest)
			validateVOMembershipRequest();
		
	}
	
	List<String> approvedGroups;
	
	@Override
	public String execute() throws Exception {
		
		stripFalseFromApprovedGroups();
		
		DECISION theDecision = DECISION.valueOf(decision.toUpperCase());
		
		new HandleVOMembershipRequest((NewVOMembershipRequest) request,
				theDecision, approvedGroups).execute();
		
		refreshPendingRequests();

		setDecision(null);

		return SUCCESS;
	}

	public List<String> getApprovedGroups() {
		return approvedGroups;
	}

	public void setApprovedGroups(List<String> approvedGroups) {
		this.approvedGroups = approvedGroups;
	}
	
}
