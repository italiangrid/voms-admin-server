package org.glite.security.voms.admin.view.actions.register;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.SUCCESS,location="register"),
	@Result(name=RegisterActionSupport.CONFIRMATION_NEEDED,location="registerWaitsConfirmation"),
	@Result(name=RegisterActionSupport.PLEASE_WAIT,location="registerLimbo"),
	@Result(name=RegisterActionSupport.ALREADY_MEMBER, location="/user/home.action", type="redirect")
})
public class StartAction extends RegisterActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String execute() throws Exception {
		
		if (!registrationEnabled())
			return REGISTRATION_DISABLED;
				
		if (CurrentAdmin.instance().getVoUser()!=null)
			return ALREADY_MEMBER;
		
		String result = checkExistingPendingRequests();
		
		if (result != null)
			return result;
		
		return SUCCESS;
	}
}
