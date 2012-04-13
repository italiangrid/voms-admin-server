package org.glite.security.voms.admin.view.actions.register;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestConfirmedEvent;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


@ParentPackage("base")
@Results( { @Result(name = BaseAction.INPUT, location = "requestAttributes"),
	@Result(name = BaseAction.SUCCESS, location = "registrationConfirmed"),
	@Result(name = BaseAction.ERROR, location = "registrationConfirmationError")
})
public class RequestAttributesAction extends RegisterActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String confirmationId;
	
	Long groupId = -1L;
	
	@Override
	public String execute() throws Exception {
		
		if (!registrationEnabled())
			return REGISTRATION_DISABLED;

		if (!getModel().getStatus().equals(STATUS.SUBMITTED)){
			addActionError("Your request has already been confirmed!");
			return ERROR;
		}
		
		if (getModel().getConfirmId().equals(confirmationId))
			request.setStatus(STATUS.CONFIRMED);
		else{
			addActionError("Wrong confirmation id!");
			return ERROR;
		}
		
		
		String manageURL = getBaseURL() + "/home/login.action";
		
		if (groupId != -1){
			VOMSGroup g = groupById(groupId);
			getModel().getRequesterInfo().addInfo("requestedGroup", g.getName());
		}

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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
}
