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
package org.glite.security.voms.admin.operations.roles;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.database.NoSuchRoleException;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.BaseAttributeRWOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.service.attributes.AttributeValue;

public class SetRoleAttributeOperation extends BaseAttributeRWOperation {

	String attributeName;

	String attributeValue;

	protected Object doExecute() {

		return VOMSRoleDAO.instance().setAttribute(__context.getRole(),
				__context.getGroup(), attributeName, attributeValue);
	}

	private SetRoleAttributeOperation(VOMSGroup g, VOMSRole r, String aName,
			String aValue) {

		super(VOMSContext.instance(g, r));

		attributeName = aName;
		attributeValue = aValue;

	}

	public static SetRoleAttributeOperation instance(String groupName,
			String roleName, AttributeValue val) {

		if (val == null)
			throw new NullArgumentException(
					"Null attribute value passed as argument!");

		VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName)
				.execute();
		VOMSRole r = (VOMSRole) FindRoleOperation.instance(roleName).execute();

		if (g == null)
			throw new NoSuchGroupException("Group '" + groupName
					+ "' not found!");

		if (r == null)
			throw new NoSuchRoleException("Role '" + roleName + "' not found!");

		return new SetRoleAttributeOperation(g, r, val.getAttributeClass()
				.getName(), val.getValue());

	}

	public static SetRoleAttributeOperation instance(VOMSGroup g, VOMSRole r,
			String aName, String aValue) {

		return new SetRoleAttributeOperation(g, r, aName, aValue);
	}

}
