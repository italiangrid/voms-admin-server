package org.glite.security.voms.admin.view.actions.role;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
	@Result(name = BaseAction.SUCCESS, location = "/role/search.action", type = "redirect"),
	@Result(name = BaseAction.INPUT, location = "roles") })
public class TestMultipleAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Override
	public String execute() throws Exception {
		
		
		return SUCCESS;
	}
}
