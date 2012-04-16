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
package org.glite.security.voms.admin.view.actions.register;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestConfirmedEvent;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.INPUT, location = "register"),
		@Result(name = BaseAction.SUCCESS, location = "registrationConfirmed"),
		@Result(name = BaseAction.ERROR, location = "registrationConfirmationError"),
		@Result(name = ConfirmRequestAction.REQUEST_ATTRIBUTES, location = "requestAttributes")
})
public class ConfirmRequestAction extends RegisterActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String REQUEST_ATTRIBUTES = "REQUEST_ATTRIBUTES";

	String confirmationId;

	@Override
	public String execute() throws Exception {

		if (!registrationEnabled())
			return REGISTRATION_DISABLED;

		if (!getModel().getStatus().equals(STATUS.SUBMITTED)){
			addActionError("Your request has already been confirmed!");
			return ERROR;
		}
		
		List<VOMSGroup> groups = VOMSGroupDAO.instance().getAll();
		
		// Enable group selection only if configured to do so
		// and if there's more than one group in the VO
		if (attributeRequestsEnabled() && groups.size() > 1){
			
			// All members are included in the root group by default, so
			// it is removed from the list of groups that could be requested
			if (groups.size() > 1)
				groups = groups.subList(1, groups.size());
			
			ServletActionContext.getRequest().setAttribute("voGroups", groups);
			
			return REQUEST_ATTRIBUTES;
		}

		if (getModel().getConfirmId().equals(confirmationId))
			request.setStatus(STATUS.CONFIRMED);
		else{
			addActionError("Wrong confirmation id!");
			return ERROR;
		}
		
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
