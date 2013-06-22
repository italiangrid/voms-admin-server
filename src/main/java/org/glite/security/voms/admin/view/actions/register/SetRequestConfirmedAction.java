package org.glite.security.voms.admin.view.actions.register;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestConfirmedEvent;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;
import org.glite.security.voms.admin.util.URLBuilder;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({
	@Result(name = BaseAction.SUCCESS, location = "registrationConfirmed"),
	@Result(name = BaseAction.ERROR, location = "registrationConfirmationError")
})
public class SetRequestConfirmedAction extends RegisterActionSupport {

	
	private static final long serialVersionUID = -3732391685550459780L;

	String confirmationId;
	

	@Override
	public String execute() throws Exception {
		
		if (!registrationEnabled())
			return REGISTRATION_DISABLED;
		
		if (!getModel().getStatus().equals(STATUS.SUBMITTED)){
			addActionError("Your request has already been confirmed!");
			return ERROR;
		}
		
		if (!getModel().getConfirmId().equals(confirmationId)){
			addActionError("Wrong confirmation id!");
			return ERROR;
		}
		
		
		getModel().setStatus(STATUS.CONFIRMED);
	
		EventManager.dispatch(new VOMembershipRequestConfirmedEvent(request,
				URLBuilder.buildLoginURL()));
		
		return SUCCESS;
	}


	
	/**
	 * @return the confirmationId
	 */
	public String getConfirmationId() {
	
		return confirmationId;
	}


	
	/**
	 * @param confirmationId the confirmationId to set
	 */
	public void setConfirmationId(String confirmationId) {
	
		this.confirmationId = confirmationId;
	}

	
}
