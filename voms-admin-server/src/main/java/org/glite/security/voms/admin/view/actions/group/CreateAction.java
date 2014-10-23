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
package org.glite.security.voms.admin.view.actions.group;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.error.VOMSSyntaxException;
import org.glite.security.voms.admin.operations.groups.CreateGroupOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "search",
    type = "redirectAction"),
  @Result(name = BaseAction.INPUT, location = "groupCreate"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE, location = "groupCreate")

})
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class CreateAction extends GroupActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  VOMSGroup group;

  String groupName;
  String parentGroupName;

  String description;
  Boolean isRestricted;

  @Override
  public void validate() {

    String candidateName = getParentGroupName() + "/" + getGroupName();

    if (getGroupName().contains("/")) {

      addFieldError("groupName", "The group name '" + getGroupName()
        + "' is invalid. It should not contain the '/' character.");
      return;
    }

    try {
      PathNamingScheme.isGroup(candidateName);

    } catch (VOMSSyntaxException e) {

      addFieldError("groupName", "'" + getGroupName()
        + "' is not a valid group name!");
      return;
    }

    VOMSGroup target = VOMSGroupDAO.instance().findByName(candidateName);

    if (target != null) {

      addFieldError("groupName", "Group '" + candidateName
        + "' already exists!");
    }

  }

  @Override
  public String execute() throws Exception {

    if (groupName == null && parentGroupName == null)
      return INPUT;

    String name = getParentGroupName() + "/" + getGroupName();

    if ("".equals(description.trim()))
      description = null;

    VOMSGroup g = (VOMSGroup) CreateGroupOperation.instance(name, description,
      isRestricted).execute();

    if (g != null)
      addActionMessage(getText("confirm.group.creation", g.getName()));

    return SUCCESS;

  }

  public VOMSGroup getModel() {

    return group;
  }

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "A name for the group is required!")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The group name field contains illegal characters!",
    expression = "^[^<>&=;]*$")
  public String getGroupName() {

    return groupName;
  }

  public void setGroupName(String groupName) {

    this.groupName = groupName;
  }

  public String getParentGroupName() {

    return parentGroupName;
  }

  public void setParentGroupName(String parentGroupName) {

    this.parentGroupName = parentGroupName;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The description field contains illegal characters!",
    expression = "^[^<>&=;]*$")
  @StringLengthFieldValidator(type = ValidatorType.FIELD, maxLength = "255",
    message = "The description field size is limited to 255 characters.")
  public String getDescription() {

    return description;
  }

  public void setDescription(String description) {

    this.description = description;
  }

  public Boolean getIsRestricted() {

    return isRestricted;
  }

  public void setIsRestricted(Boolean isRestricted) {

    this.isRestricted = isRestricted;
  }

  @Override
  public void prepare() throws Exception {

  }

}
