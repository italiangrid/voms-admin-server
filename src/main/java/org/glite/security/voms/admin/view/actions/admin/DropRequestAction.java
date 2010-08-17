package org.glite.security.voms.admin.view.actions.admin;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.requests.DeleteVOMembershipRequestOperation;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "pendingRequests.jsp"),
		@Result(name = BaseAction.INPUT, location = "pendingRequests.jsp") })
public class DropRequestAction extends RequestActionSupport {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void validate() {
		
		if (!(request instanceof NewVOMembershipRequest))
			addActionError("Only VO membership request can be dropped with this action");
		
	}

	@Override
	public String execute() throws Exception {
		
		DeleteVOMembershipRequestOperation op = new DeleteVOMembershipRequestOperation((NewVOMembershipRequest)request);
		op.execute();
		addActionMessage("Request dropped!");
		refreshPendingRequests();
		return SUCCESS;
	}
}
