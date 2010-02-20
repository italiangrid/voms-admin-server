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

import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.util.PathNamingScheme;

public class FindGroupOperation extends BaseVomsOperation {

	Long id = null;

	String name = null;

	private FindGroupOperation(Long id) {
		this.id = id;

	}

	private FindGroupOperation(String name) {

		this.name = name;
	}

	protected Object doExecute() {

		if (id != null)
			return VOMSGroupDAO.instance().findById(id);

		if (name != null)
			return VOMSGroupDAO.instance().findByName(name);

		return null;
	}

	public static FindGroupOperation instance(Long id) {

		return new FindGroupOperation(id);
	}

	public static FindGroupOperation instance(String name) {

		return new FindGroupOperation(name);
	}

	protected void setupPermissions() {
		if (name != null) {

			String parentGroupName = PathNamingScheme.getParentGroupName(name);

			VOMSContext ctxt = VOMSContext.instance(VOMSGroupDAO.instance()
					.findByName(parentGroupName));

			addRequiredPermission(ctxt, VOMSPermission
					.getContainerReadPermission());

		} else if (id != null) {

			VOMSGroup parentGroup = VOMSGroupDAO.instance().findById(id)
					.getParent();

			VOMSContext ctxt = VOMSContext.instance(parentGroup);

			addRequiredPermission(ctxt, VOMSPermission
					.getContainerReadPermission());

		}

		return;
	}
}
