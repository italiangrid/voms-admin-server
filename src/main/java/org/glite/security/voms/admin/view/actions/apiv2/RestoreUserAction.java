package org.glite.security.voms.admin.view.actions.apiv2;

import java.util.Collection;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.glite.security.voms.admin.operations.users.RestoreUserOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("json")
@Results( { @Result(name = BaseAction.SUCCESS, type = "json") })
public class RestoreUserAction extends RestUserAction{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public String execute() throws Exception {
		
		RestoreUserOperation.instance(getModel()).execute();
		
		addActionMessage(String.format("User %s restored succesfully.", getModel().getShortName()));
		return SUCCESS;
	}
	
	
	@JSON(serialize=true)
	public Collection<String> getActionMessages() {
		
		return super.getActionMessages();
	}
}
