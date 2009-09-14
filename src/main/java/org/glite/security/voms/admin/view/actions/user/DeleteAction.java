package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.users.DeleteUserOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "search", type = "chain"),
		@Result(name = BaseAction.INPUT, location = "usersDetail") })
public class DeleteAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {

		DeleteUserOperation.instance(getModel()).execute();
		
		
		addActionMessage("User '"+getModel().getShortName()+"' deleted.");

		return SUCCESS;
	}

}
