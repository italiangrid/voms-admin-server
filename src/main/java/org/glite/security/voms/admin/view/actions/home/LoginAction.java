package org.glite.security.voms.admin.view.actions.home;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage(value = "base")
@Results( {
		@Result(name = "admin-home", location = "/admin/home.action", type = "redirect"),
		@Result(name = "user-home", location = "/user/home.action", type = "redirect"),
		@Result(name = "guest-home", location = "guestHome"),
		@Result(name = "register", location = "/register/start.action", type = "redirect") })
public class LoginAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {

		CurrentAdmin admin = CurrentAdmin.instance();

		if (admin.isVOAdmin())
			return "admin-home";
		else if (admin.isVoUser())
			return "user-home";
		else
			return "register";
	}

}
