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
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSOperation;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.util.PathNamingScheme;

public class DeleteGroupOperation extends BaseVomsOperation {

	String groupName = null;

	Long groupId;

	VOMSGroup group;

	private DeleteGroupOperation(String groupName) {

		this.groupName = groupName;
	}

	private DeleteGroupOperation(VOMSGroup g) {

		this.group = g;
	}

	private DeleteGroupOperation(Long id) {

		this.groupId = id;

	}

	protected Object doExecute() {

		if (group == null) {
			if (groupName != null)
				group = VOMSGroupDAO.instance().findByName(groupName);
			else
				group = VOMSGroupDAO.instance().findById(groupId);
		}

		if (group == null) {

			String msg;

			if (groupName == null)
				msg = "Group having id '"
						+ groupId
						+ "' not found in org.glite.security.voms.admin.database";
			else
				msg = "Group '"
						+ groupName
						+ "' not found in org.glite.security.voms.admin.database.";

			throw new NoSuchGroupException(msg);

		}

		VOMSGroupDAO.instance().delete(group);
		return group;
	}

	public static DeleteGroupOperation instance(String groupName) {

		return new DeleteGroupOperation(groupName);
	}

	public static VOMSOperation instance(VOMSGroup g) {

		return new DeleteGroupOperation(g);
	}

	public static VOMSOperation instance(Long id) {

		return new DeleteGroupOperation(id);

	}

	protected void setupPermissions() {

		VOMSContext ctxt;

		if (group == null) {

			if (groupName != null) {
				String parentGroupName = PathNamingScheme
						.getParentGroupName(groupName);

				VOMSGroup parentGroup = VOMSGroupDAO.instance().findByName(
						parentGroupName);

				if (parentGroup == null)
					throw new NoSuchGroupException(
							"Group '"
									+ parentGroupName
									+ "' not found in org.glite.security.voms.admin.database!");

				ctxt = VOMSContext.instance(parentGroup);

			} else {

				VOMSGroup g = VOMSGroupDAO.instance().findById(groupId);

				if (g == null)
					throw new NoSuchGroupException(
							"Group having id '"
									+ groupId
									+ "' not found in org.glite.security.voms.admin.database!");

				ctxt = VOMSContext.instance(g.getParent());
			}

		} else
			ctxt = VOMSContext.instance(group.getParent());

		addRequiredPermission(ctxt, VOMSPermission.getContainerRWPermissions());

	}
}
