/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.view.actions.user;

import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
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
		
		MembershipRemovalRequest req = reqDAO.createMembershipRemovalRequest(getModel(), reason, getDefaultFutureDate());
		
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
