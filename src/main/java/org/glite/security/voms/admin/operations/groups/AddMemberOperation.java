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
package org.glite.security.voms.admin.operations.groups;

import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.error.NoSuchUserException;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class AddMemberOperation extends BaseVomsOperation {

	VOMSUser user;

	VOMSGroup group;

	public AddMemberOperation(VOMSUser u, VOMSGroup g) {
		this.user = u;
		this.group = g;
	}

	public Object doExecute() {

		// Add user to parent groups (if any) if she's not already there
		if (!group.isRootGroup()) {

			if (!user.isMember(group.getParent())) {
				instance(user, group.getParent()).execute();
			}
		}

		VOMSUserDAO.instance().addToGroup(user, group);

		return null;
	}

	public static AddMemberOperation instance(VOMSUser u, VOMSGroup g) {

		return new AddMemberOperation(u, g);
	}

	public static AddMemberOperation instance(String groupName,
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
		return new AddMemberOperation(u, g);

	}

	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.instance(group.getParent()),
				VOMSPermission.getContainerReadPermission());
		addRequiredPermission(VOMSContext.instance(group), VOMSPermission
				.getMembershipRWPermissions());

	}
}
