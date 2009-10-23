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
package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.database.NoSuchRoleException;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.roles.FindRoleOperation;

public class DismissRoleOperation extends BaseVomsOperation {

	VOMSUser user;

	VOMSGroup group;

	VOMSRole role;
	
	private DismissRoleOperation(Long userId, String roleFqan){
		
		user = (VOMSUser) FindUserOperation.instance(userId).execute();
		
		VOMSContext context = VOMSContext.instance(roleFqan);
		
		group = context.getGroup();
		role = context.getRole();
	}

	private DismissRoleOperation(VOMSUser u, VOMSGroup g, VOMSRole r) {

		this.user = u;
		this.group = g;
		this.role = r;
	}

	public VOMSContext getContext() {

		return VOMSContext.instance(group);
	}

	public VOMSPermission getRequiredPermission() {

		return VOMSPermission.getContainerRWPermissions();
	}

	public Object doExecute() {

		VOMSUserDAO.instance().dismissRole(user, group, role);

		return null;
	}

	public static DismissRoleOperation instance(String groupName,
			String roleName, String userName, String caDn) {

		VOMSUser u = (VOMSUser) FindUserOperation.instance(userName, caDn)
				.execute();
		VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName)
				.execute();
		VOMSRole r = (VOMSRole) FindRoleOperation.instance(roleName).execute();

		if (u == null)
			throw new NoSuchUserException("User '" + userName + "," + caDn
					+ "' not found in this vo.");

		if (g == null)
			throw new NoSuchGroupException("Group '" + groupName
					+ "' does not exist in this vo.");

		if (r == null)
			throw new NoSuchRoleException("Role '" + roleName
					+ "' does not exists in this vo.");

		return new DismissRoleOperation(u, g, r);

	}

	public static DismissRoleOperation instance(VOMSUser u, VOMSGroup g,
			VOMSRole r) {

		return new DismissRoleOperation(u, g, r);
	}

	public static DismissRoleOperation instance(Long userId, String roleFqan){
		
		return new DismissRoleOperation(userId, roleFqan);
	}
	
	protected void setupPermissions() {

		addRequiredPermission(VOMSContext.instance(group), VOMSPermission
				.getContainerReadPermission().setMembershipRWPermission());

	}
}
