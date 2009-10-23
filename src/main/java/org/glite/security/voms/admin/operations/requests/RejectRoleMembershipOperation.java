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
package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.common.IllegalRequestStateException;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.RoleMembershipRejectedEvent;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class RejectRoleMembershipOperation extends BaseVomsOperation {

	RoleMembershipRequest request;
	
	public static RejectRoleMembershipOperation instance(RoleMembershipRequest req){
		return new RejectRoleMembershipOperation(req);
	}
	
	private RejectRoleMembershipOperation(RoleMembershipRequest req) {
		this.request = req;
	}
	
	
	@Override
	protected Object doExecute() {
		if (!request.getStatus().equals(StatusFlag.SUBMITTED))
			throw new IllegalRequestStateException(
					"Illegal state for request: " + request.getStatus());
		
		request.reject();
		EventManager.dispatch(new RoleMembershipRejectedEvent(request));
		
		return request;
		
	}

	@Override
	protected void setupPermissions() {
		VOMSGroup g = VOMSGroupDAO.instance()
		.findByName(request.getGroupName());

		if (g == null) {
			addRequiredPermissionOnAllGroups(VOMSPermission
					.getRequestsRWPermissions());
		} else {
			VOMSRole r = VOMSRoleDAO.instance().findByName(request.getRoleName());
			
			if (r != null){
				addRequiredPermissionOnPath(g, VOMSPermission.getContainerReadPermission());
				addRequiredPermission(VOMSContext.instance(g, r), VOMSPermission.getRequestsRWPermissions());
			}else{
				addRequiredPermissionOnPath(g, VOMSPermission.getContainerReadPermission());
				addRequiredPermission(VOMSContext.instance(g), VOMSPermission.getRequestsRWPermissions());
			}
		}
	}

}
