package org.glite.security.voms.admin.view.actions.register;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestConfirmedEvent;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.INPUT,location="register"),
	@Result(name=BaseAction.SUCCESS,location="registrationConfirmed"),
})
public class ConfirmRequestAction extends RegisterActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	String confirmId;
	
	@Override
	public String execute() throws Exception {
		
		if (!registrationEnabled())
			return REGISTRATION_DISABLED;
		
		if (!request.getStatus().equals(StatusFlag.SUBMITTED))
			throw new IllegalArgumentException("Cannot confirm an already confirmed request!");
		
		
		if (request.getConfirmId().equals(confirmId))
			request.setStatus(StatusFlag.CONFIRMED);
		
		String manageURL = getBaseURL()+"/home/login.action";
		
		EventManager.dispatch(new VOMembershipRequestConfirmedEvent(request,manageURL));
		return SUCCESS;
	}

	@RequiredFieldValidator(type=ValidatorType.FIELD, message="A confirmation id is required!")
	public String getConfirmId() {
		return confirmId;
	}

	public void setConfirmId(String confirmId) {
		this.confirmId = confirmId;
	}
	
	

}
