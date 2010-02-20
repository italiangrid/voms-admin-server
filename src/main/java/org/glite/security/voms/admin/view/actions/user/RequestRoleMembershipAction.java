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
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.database.NoSuchRoleException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.RoleMembershipSubmittedEvent;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;

@ParentPackage("base")
@Results({
	
	@Result(name=UserActionSupport.SUCCESS,location="mappingsRequest.jsp"),
	@Result(name=UserActionSupport.ERROR,location="mappingsRequest.jsp"),
	@Result(name=UserActionSupport.INPUT,location="mappingsRequest.jsp")
})
public class RequestRoleMembershipAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Long groupId;
	Long roleId;
	
	@Override
	public void validate() {
		
		RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
		
		VOMSGroup g = groupById(groupId);
		VOMSRole r = roleById(roleId);
		
		if (g == null)
			throw new NoSuchGroupException("Group with id '"+groupId+"' not found!");
		
		if (r == null)
			throw new NoSuchRoleException("Role with id '"+roleId+"' not found!");
		
		
		if (model.hasRole(g, r))
			addActionError(getText("role_request.user.already_member", new String[]{model.toString(), r.getName(), g.getName()}));
		
		if (reqDAO.userHasPendingRoleMembershipRequest(model, g, r))
			addActionError(getText("role_request.user.has_pending_request", new String[]{model.toString(), r.getName(), g.getName()}));
		
	}
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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
		VOMSRole r = roleById(roleId);
		
		RoleMembershipRequest request = reqDAO.createRoleMembershipRequest(model, g, r, getDefaultFutureDate());
		EventManager.dispatch(new RoleMembershipSubmittedEvent(request, getHomeURL()));
		
		refreshPendingRequests();
		
		return SUCCESS;
	}
}
