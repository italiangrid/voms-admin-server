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
package org.glite.security.voms.admin.view.actions.group;

import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.groups.DeleteGroupOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "search", type = "chain"),
  @Result(name = BaseAction.INPUT, location = "search", type = "chain"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE, location = "groups") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class DeleteAction extends GroupActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  @Override
  public void validate() {

    VOMSGroupDAO dao = VOMSGroupDAO.instance();

    List<VOMSGroup> childrenGroups = dao.getChildren(getModel());

    if (!childrenGroups.isEmpty())
      addActionError("The group '"
        + getModel().getName()
        + "' cannot be deleted since it contains subgroups. Delete subgroups first.");
  }

  @Override
  public String execute() throws Exception {

    VOMSGroup g = (VOMSGroup) DeleteGroupOperation.instance(getModel())
      .execute();

    if (g != null)
      addActionMessage(getText("confirm.group.deletion",
        new String[] { g.getName() }));

    return SUCCESS;
  }

}
