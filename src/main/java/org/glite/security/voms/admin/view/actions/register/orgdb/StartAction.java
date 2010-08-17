package org.glite.security.voms.admin.view.actions.register.orgdb;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.view.actions.BaseAction;


@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "orgdbRegister")
})
public class StartAction extends
		OrgDbRegisterActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}

}
