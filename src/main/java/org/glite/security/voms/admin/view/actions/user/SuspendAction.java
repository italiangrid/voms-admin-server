package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.UserRestoredEvent;
import org.glite.security.voms.admin.event.user.UserSuspendedEvent;
import org.glite.security.voms.admin.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.operations.users.RestoreUserOperation;
import org.glite.security.voms.admin.operations.users.SuspendUserOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.SUCCESS,location="/user/search.action", type="redirect"),
	@Result(name=BaseAction.INPUT, location="userSuspend")
})
public class SuspendAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String suspensionReason;
	
	@Override
	public String execute() throws Exception {
		
		SuspensionReason r = SuspensionReason.OTHER;
		r.setMessage(getSuspensionReason());
		
		SuspendUserOperation.instance(getModel(), r).execute();
		EventManager.dispatch(new UserSuspendedEvent(getModel(), r));
		
		return SUCCESS;
	}

	@Action("restore")
	public String restore() throws Exception{
		
		RestoreUserOperation.instance(getModel()).execute();
		
		EventManager.dispatch(new UserRestoredEvent(getModel()));
		
		return SUCCESS;
	}
	
	
	public String getSuspensionReason() {
		return suspensionReason;
	}

	
	public void setSuspensionReason(String suspensionReason) {
		this.suspensionReason = suspensionReason;
	}
	
	

}
