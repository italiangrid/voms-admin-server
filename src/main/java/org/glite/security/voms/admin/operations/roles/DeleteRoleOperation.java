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
package org.glite.security.voms.admin.operations.roles;

import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class DeleteRoleOperation extends BaseVomsOperation {

	String roleName = null;

	Long roleId;

	VOMSRole role = null;

	private DeleteRoleOperation(Long roleId) {

		this.roleId = roleId;
	}

	private DeleteRoleOperation(String roleName) {

		this.roleName = roleName;
	}

	private DeleteRoleOperation(VOMSRole r) {

		this.role = r;
	}

	protected Object doExecute() {

		if (role == null) {

			if (roleName != null)
				return VOMSRoleDAO.instance().delete(roleName);
			else
				return VOMSRoleDAO.instance().delete(roleId);
		}

		else
			return VOMSRoleDAO.instance().delete(role);

	}

	public static DeleteRoleOperation instance(String name) {

		return new DeleteRoleOperation(name);
	}

	public static DeleteRoleOperation instance(VOMSRole r) {

		return new DeleteRoleOperation(r);
	}

	public static DeleteRoleOperation instance(Long id) {

		return new DeleteRoleOperation(id);
	}

	protected void setupPermissions() {

		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
				.getContainerRWPermissions());

	}
}
