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
package org.glite.security.voms.admin.view.actions.acl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.acls.SaveACLEntryOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "manage", type = "chain"),
		@Result(name = BaseAction.INPUT, location = "editACLEntry") })
public class EditEntryAction extends ACLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<String> selectedPermissions;
	VOMSPermission permission;

	@Override
	public String execute() throws Exception {

		String permString;

		if (selectedPermissions == null)
			permString = "NONE";
		else if (selectedPermissions.contains("ALL"))
			permString = "ALL";
		else
			permString = StringUtils.join(selectedPermissions, "|");

		
		VOMSPermission perm = VOMSPermission.fromString(permString);
		
		limitUnauthenticatedClientPermissions(perm);
			
		SaveACLEntryOperation op = SaveACLEntryOperation.instance(getModel(),
				admin, perm, propagate);

		op.execute();

		return SUCCESS;
	}

	public void prepareInput() throws Exception {
		prepare();

		if (permission == null)
			permission = getModel().getPermissions(getAdmin());

	}

	public VOMSPermission getPermission() {
		return permission;
	}

	public void setPermission(VOMSPermission permission) {
		this.permission = permission;
	}

	public List<String> getSelectedPermissions() {
		return selectedPermissions;
	}

	public void setSelectedPermissions(List<String> selectedPermissions) {
		this.selectedPermissions = selectedPermissions;
	}

}
