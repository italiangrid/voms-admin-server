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
package org.glite.security.voms.admin.operations.groups;

import java.util.Iterator;
import java.util.List;

import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.users.FindUserOperation;

public class RemoveMemberOperation extends BaseVomsOperation {

	VOMSUser user;

	VOMSGroup group;

	private RemoveMemberOperation(VOMSUser u, VOMSGroup g) {
		user = u;
		group = g;

	}

	protected Object doExecute() {

		// Remove from children group first, if there are some

		List childrenGroups = (List) ListChildrenGroupsOperation
				.instance(group).execute();

		Iterator i = childrenGroups.iterator();

		while (i.hasNext()) {
			VOMSGroup childGroup = (VOMSGroup) i.next();
			if (user.isMember(childGroup))
				instance(user, childGroup).execute();
		}

		VOMSUserDAO.instance().removeFromGroup(user, group);
		return null;
	}

	public static RemoveMemberOperation instance(VOMSUser u, VOMSGroup g) {

		return new RemoveMemberOperation(u, g);
	}

	public static RemoveMemberOperation instance(String groupName,
			String username, String caDn) {

		VOMSUser u = (VOMSUser) FindUserOperation.instance(username, caDn)
				.execute();
		VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName)
				.execute();

		if (u == null)
			throw new NoSuchUserException("User '" + username + "," + caDn
					+ "' not found in this vo.");

		if (g == null)
			throw new NoSuchGroupException("Group '" + groupName
					+ "' does not exist in this vo.");

		return new RemoveMemberOperation(u, g);

	}

	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.instance(group.getParent()),
				VOMSPermission.getContainerReadPermission());
		addRequiredPermission(VOMSContext.instance(group), VOMSPermission
				.getMembershipRWPermissions().setContainerReadPermission());
	}

}
