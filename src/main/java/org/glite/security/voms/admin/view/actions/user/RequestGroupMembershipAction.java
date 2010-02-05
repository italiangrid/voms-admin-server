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
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.GroupMembershipSubmittedEvent;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;

@ParentPackage("base")
@Results({
	
	@Result(name=UserActionSupport.SUCCESS,location="mappingsRequest.jsp"),
	@Result(name=UserActionSupport.ERROR,location="mappingsRequest.jsp"),
	@Result(name=UserActionSupport.INPUT,location="mappingsRequest.jsp")
})
public class RequestGroupMembershipAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Long groupId;
	
	
	@Override
	public void validate() {
		
		RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
		
		VOMSGroup g = groupById(groupId);
		
		if (g == null)
			throw new NoSuchGroupException("Group with id '"+groupId+"' not found!");
		
		if (model.isMember(g))
			addActionError(getText("group_request.user.already_member", new String[]{model.toString(),g.getName()}));
		
		
		if (reqDAO.userHasPendingGroupMembershipRequest(model, g))
			addActionError(getText("group_request.user.has_pending_request",new String[]{model.toString(),g.getName()}));
	}
		
	
	
	@Override
	public String execute() throws Exception {
		
		if (!VOMSConfiguration.instance().getBoolean(
				VOMSConfiguration.REGISTRATION_SERVICE_ENABLED, true))
			return "registrationDisabled";
		
		if (hasActionErrors())
			return ERROR;

		RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
		VOMSGroup g = groupById(groupId);

		GroupMembershipRequest req = reqDAO.createGroupMembershipRequest(getModel(), g, getDefaultFutureDate());
		EventManager.dispatch(new GroupMembershipSubmittedEvent(req, getHomeURL()));
		
		refreshPendingRequests();
		
		return SUCCESS;
	}

	public Long getGroupId() {
		return groupId;
	}	
	
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}
