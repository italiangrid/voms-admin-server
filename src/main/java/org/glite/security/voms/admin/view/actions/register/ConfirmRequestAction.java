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
@Results( { @Result(name = BaseAction.INPUT, location = "register"),
		@Result(name = BaseAction.SUCCESS, location = "registrationConfirmed"),
		@Result(name = BaseAction.ERROR, location = "registrationConfirmationError")
})
public class ConfirmRequestAction extends RegisterActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String confirmationId;

	@Override
	public String execute() throws Exception {

		if (!registrationEnabled())
			return REGISTRATION_DISABLED;

		if (!getModel().getStatus().equals(StatusFlag.SUBMITTED))
			throw new IllegalArgumentException(
					"Cannot confirm an already confirmed request!");

		if (getModel().getConfirmId().equals(confirmationId))
			request.setStatus(StatusFlag.CONFIRMED);
		else
			return ERROR;

		String manageURL = getBaseURL() + "/home/login.action";

		EventManager.dispatch(new VOMembershipRequestConfirmedEvent(request,
				manageURL));
		return SUCCESS;
	}

	
	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "A confirmation id is required!")
	public String getConfirmationId() {
		return confirmationId;
	}

	public void setConfirmationId(String confirmationId) {
		this.confirmationId = confirmationId;
	}

}
