package org.glite.security.voms.admin.view.actions.register;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
	@Result(name = BaseAction.SUCCESS, location = "/register/start.action", type = "redirect") })
public class Index extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {
		
		return BaseAction.SUCCESS;
	}
}
