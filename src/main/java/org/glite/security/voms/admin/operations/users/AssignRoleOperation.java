/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
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

public class AssignRoleOperation extends BaseVomsOperation {

	VOMSUser user;

	VOMSGroup group;

	VOMSRole role;

	private AssignRoleOperation(VOMSUser u, VOMSGroup g, VOMSRole r) {

		user = u;
		group = g;
		role = r;
	}

	public Object doExecute() {

		VOMSUserDAO.instance().assignRole(user, group, role);
		return null;
	}

	public static AssignRoleOperation instance(VOMSUser u, VOMSGroup g,
			VOMSRole r) {

		return new AssignRoleOperation(u, g, r);
	}

	public static AssignRoleOperation instance(String groupName,
			String roleName, String userName, String caDn) {

		VOMSUser u = (VOMSUser) FindUserOperation.instance(userName, caDn)
				.execute();
		VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName)
				.execute();
		VOMSRole r = (VOMSRole) FindRoleOperation.instance(roleName).execute();

		if (u == null)
			throw new NoSuchUserException("User '" + userName + "," + caDn
					+ "' not found in org.glite.security.voms.admin.database.");
		if (g == null)
			throw new NoSuchGroupException("Group '" + groupName
					+ "' not found in org.glite.security.voms.admin.database.");
		if (r == null)
			throw new NoSuchRoleException("Role '" + roleName
					+ "' not found in org.glite.security.voms.admin.database.");

		return new AssignRoleOperation(u, g, r);

	}

	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.instance(group), VOMSPermission
				.getContainerReadPermission().setMembershipRWPermission());
	}

}
