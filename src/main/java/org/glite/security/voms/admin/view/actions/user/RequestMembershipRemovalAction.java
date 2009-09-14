package org.glite.security.voms.admin.view.actions.user;

import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.MembershipRemovalSubmittedEvent;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "userHome"),
		@Result(name = BaseAction.INPUT, location = "requestMembershipRemoval"),
		@Result(name = "registrationDisabled", location = "userHome")
})

public class RequestMembershipRemovalAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String reason;
	
	@Override
	public void validate() {
		VOMSUser u = CurrentAdmin.instance().getVoUser();
		
		if (!getModel().equals(u))
			addActionError("You cannot submit a membership removal request for another user!");
		
		RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
		
		if (reqDAO.userHasPendingMembershipRemovalRequest(getModel()))
			addActionError("User has pending membership removal requests!");
		
	}
	@Override
	public String execute() throws Exception {
		
		if (!VOMSConfiguration.instance().getBoolean(
				VOMSConfiguration.REGISTRATION_SERVICE_ENABLED, true))
			return "registrationDisabled";
		
		RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
		
		MembershipRemovalRequest req = reqDAO.createMembershipRemovalRequest(getModel(), reason, getFutureDate(new Date(), Calendar.DAY_OF_YEAR, 5));
		
		EventManager.dispatch(new MembershipRemovalSubmittedEvent(req, getHomeURL()));
		
		refreshPendingRequests();
		
		return SUCCESS;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
}
