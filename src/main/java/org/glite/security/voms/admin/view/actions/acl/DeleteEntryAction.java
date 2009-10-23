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

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.ACLDAO;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.operations.acls.DeleteACLEntryOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "manage", type = "chain"),
		@Result(name = BaseAction.INPUT, location = "deleteACLEntry") })
public class DeleteEntryAction extends ACLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {

		DeleteACLEntryOperation op = DeleteACLEntryOperation.instance(
				getModel(), getAdmin(), getPropagate());
		op.execute();

		ACLDAO dao = ACLDAO.instance();
		// Delete admin if it doesn't have any active permissions
		if (!admin.isInternalAdmin()) {
			if (!dao.hasActivePermissions(admin))
				VOMSAdminDAO.instance().delete(admin);
		}

		// Delete default ACL if it's empty
		if (model.isDefautlACL() && model.getPermissions().isEmpty())
			dao.delete(model);

		return SUCCESS;
	}

}
