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

import java.util.Iterator;
import java.util.List;

import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.persistence.error.HibernateFactory;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;

public class CreateRoleOperation extends BaseVomsOperation {

	String roleName;

	private CreateRoleOperation(String roleName) {

		this.roleName = roleName;
	}

	private void setupACLs(VOMSRole r) {

		List groups = (List) ListGroupsOperation.instance().execute();

		Iterator groupsIter = groups.iterator();

		while (groupsIter.hasNext())
			r.importACL((VOMSGroup) groupsIter.next());

	}

	protected Object doExecute() {

		VOMSRole r = VOMSRoleDAO.instance().create(roleName);

		setupACLs(r);

		HibernateFactory.getSession().save(r);

		return r;
	}

	public static CreateRoleOperation instance(String roleName) {

		return new CreateRoleOperation(roleName);
	}

	protected void setupPermissions() {

		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
				.getContainerRWPermissions());

	}

}
