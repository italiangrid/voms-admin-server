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

import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.util.PathNamingScheme;

public class FindContextOperation extends BaseVomsOperation {

	VOMSContext theContext = null;

	String contextString = null;

	private FindContextOperation(String contextString) {

		if (contextString == null)
			throw new NullArgumentException("contextString cannot be null!");

		PathNamingScheme.checkSyntax(contextString);

		this.contextString = contextString;
	}

	public static FindContextOperation instance(String contextString) {

		return new FindContextOperation(contextString);
	}

	@Override
	protected Object doExecute() {

		String groupName = PathNamingScheme.getGroupName(contextString);
		VOMSGroup g = VOMSGroupDAO.instance().findByName(groupName);

		if (PathNamingScheme.isQualifiedRole(contextString)) {

			String roleName = PathNamingScheme.getRoleName(contextString);
			VOMSRole r = VOMSRoleDAO.instance().findByName(roleName);

			return VOMSContext.instance(g, r);
		}

		return VOMSContext.instance(g);
	}

	@Override
	protected void setupPermissions() {

		// A group part must be present in the contextString
		VOMSGroup g = VOMSGroupDAO.instance().findByName(
				PathNamingScheme.getGroupName(contextString));
		addRequiredPermissionOnPath(g, VOMSPermission
				.getContainerReadPermission());

	}

}
