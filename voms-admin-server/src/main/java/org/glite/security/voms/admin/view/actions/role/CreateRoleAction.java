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
package org.glite.security.voms.admin.view.actions.role;

import java.util.Arrays;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.roles.CreateRoleOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "search", type="redirectAction"),
  @Result(name = BaseAction.INPUT, location = "roleCreate"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE, location = "search",
  type = "redirectAction")
})

@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" , "store.operationMode", "STORE"})
public class CreateRoleAction extends RoleActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  VOMSRole role;
  String roleName;

  public VOMSRole getModel() {

    return role;
  }

  public void prepare() throws Exception {

  }

  public String execute() throws Exception {

    VOMSRole r = (VOMSRole) CreateRoleOperation.instance(getRoleName())
      .execute();
    if (r != null)
      addActionMessage(getText("confirm.role.creation", Arrays.asList(r.getName())));

    return SUCCESS;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "A name for the role is required!")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The role name field contains illegal characters!",
    regex = "^[\\w.-]+$")
  public String getRoleName() {

    return roleName;
  }

  public void setRoleName(String roleName) {

    this.roleName = roleName;
  }

  @Override
  public void validate() {

    if (getRoleName().contains("/")) {

      addFieldError("roleName", "'" + getRoleName()
        + "' is not a valid VOMS role name.");
      return;
    }

    VOMSRole r = VOMSRoleDAO.instance().findByName(roleName);
    if (r != null)
      addFieldError("roleName", "Role '" + roleName + "' already exists!");

  }

  @Override
  @Action("create-role-input")
  public String input() throws Exception {
    return super.input();
  }
}
