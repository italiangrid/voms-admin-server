/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.groups.AddMemberOperation;
import org.glite.security.voms.admin.operations.groups.RemoveMemberOperation;
import org.glite.security.voms.admin.operations.users.AssignRoleOperation;
import org.glite.security.voms.admin.operations.users.DismissRoleOperation;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "mappings.jsp"),
  @Result(name = BaseAction.INPUT, location = "mappings.jsp"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE, location = "mappings.jsp") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "assignRole,addToGroup,removeFromGroup,dismissRole" })
public class MembershipActions extends UserActionSupport {

  public static final Logger log = LoggerFactory
    .getLogger(MembershipActions.class);

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  Long groupId;
  Long roleId;

  @Action("add-to-group")
  public String addToGroup() throws Exception {

    VOMSGroup g = groupById(groupId);

    AddMemberOperation.instance(getModel(), g).execute();

    addActionMessage(getText("confirm.user.add_to_group",
      new String[] { model.getShortName(), g.toString() }));

    return SUCCESS;
  }

  @Action(value = "remove-from-group")
  public String removeFromGroup() throws Exception {

    VOMSGroup g = groupById(groupId);

    RemoveMemberOperation.instance(getModel(), g).execute();

    addActionMessage(getText("confirm.user.remove_from_group", new String[] {
      model.getShortName(), g.toString() }));

    return SUCCESS;

  }

  @Action(value = "assign-role")
  public String assignRole() throws Exception {

    VOMSGroup g = groupById(groupId);
    VOMSRole r = roleById(roleId);

    AssignRoleOperation.instance(getModel(), g, r).execute();

    addActionMessage(getText("confirm.user.assign_role", new String[] {
      getModel().getShortName(), r.toString(), g.toString() }));

    return SUCCESS;

  }

  @Action("dismiss-role")
  public String dismissRole() throws Exception {

    VOMSGroup g = groupById(groupId);
    VOMSRole r = roleById(roleId);

    DismissRoleOperation.instance(getModel(), g, r).execute();
    addActionMessage(getText("confirm.user.dismiss_role", new String[] {
      getModel().getShortName(), r.toString(), g.toString() }));

    return SUCCESS;

  }

  public Long getGroupId() {

    return groupId;
  }

  public void setGroupId(Long groupId) {

    this.groupId = groupId;
  }

  public Long getRoleId() {

    return roleId;
  }

  public void setRoleId(Long roleId) {

    this.roleId = roleId;
  }

}
