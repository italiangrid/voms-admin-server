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
package org.glite.security.voms.admin.view.actions.apiv2;

import java.util.Collection;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.glite.security.voms.admin.operations.groups.CreateGroupOperation;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("json")
@Results({ @Result(name = BaseAction.SUCCESS, type = "json"),
  @Result(name = BaseAction.INPUT, type = "json") })
public class CreateGroupAction extends BaseAction {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  String groupName;
  String groupDescription;

  @Override
  public String execute() throws Exception {

    if ("".equals(groupDescription.trim())) {
      groupDescription = null;
    }

    CreateGroupOperation op = CreateGroupOperation.instance(groupName,
      groupDescription, false);
    VOMSGroup g = (VOMSGroup) op.execute();

    addActionMessage(String
      .format("Group %s created succesfully.", g.getName()));

    return SUCCESS;
  }

  @JSON(serialize = false)
  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please provide a name for the group.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The group name field contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getGroupName() {

    return groupName;
  }

  public void setGroupName(String groupName) {

    this.groupName = groupName;
  }

  @JSON(serialize = false)
  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please provide a description for the group.")
  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The group description field contains illegal characters!",
    regex = "^[^<>&=;]*$")
  public String getGroupDescription() {

    return groupDescription;
  }

  public void setGroupDescription(String groupDescription) {

    this.groupDescription = groupDescription;
  }

  @JSON(serialize = true)
  @Override
  public Collection<String> getActionMessages() {

    return super.getActionMessages();
  }
}
