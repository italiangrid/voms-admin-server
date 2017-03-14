/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
 */
package org.glite.security.voms.admin.view.actions.acl;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.acls.DeleteACLEntryOperation;
import org.glite.security.voms.admin.persistence.dao.ACLDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "manage", type = "chain"),
  @Result(name = BaseAction.INPUT, location = "deleteACLEntry") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class DeleteEntryAction extends ACLActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  @Override
  public String execute() throws Exception {

    DeleteACLEntryOperation op = DeleteACLEntryOperation.instance(getModel(),
      getAdmin(), getPropagate());
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
