package org.glite.security.voms.admin.view.actions.request;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.operations.requests.ApproveVOMembershipOperation;
import org.glite.security.voms.admin.operations.requests.RejectVOMembershipOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.SUCCESS,location="adminHome"),
	@Result(name=BaseAction.INPUT,location="requestDetails")
})
public class DecisionAction extends RequestActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String decision;
	
	@Override
	public String execute() throws Exception {
		
		if (request instanceof NewVOMembershipRequest){
			
			if (decision.equals("approve"))
				ApproveVOMembershipOperation.instance((NewVOMembershipRequest)request).execute();
			else
				RejectVOMembershipOperation.instance((NewVOMembershipRequest)request).execute();
		}		
		
		return SUCCESS;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	
}
