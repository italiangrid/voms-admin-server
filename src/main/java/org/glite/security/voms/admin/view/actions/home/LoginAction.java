package org.glite.security.voms.admin.view.actions.home;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;


@ParentPackage(value="base")
@Results({
	@Result(name="admin-home", location="adminHome"), 
	@Result(name="user-home", location="userHome"),
	@Result(name="guest-home", location="guestHome"),
	@Result(name="registration", location="registration")
})

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
		else if (admin.canBrowseVO())
			return "guest-home";
		
		return "registration";
	}

}
