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
package org.glite.security.voms.admin.view.actions.role;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.roles.DeleteRoleAttributeOperation;
import org.glite.security.voms.admin.operations.roles.SetRoleAttributeOperation;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "attributes.jsp"),
  @Result(name = BaseAction.INPUT, location = "attributes.jsp"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE,
    location = "attributes.jsp") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "setAttribute,deleteAttribute" })
public class AttributeActions extends RoleActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  Long groupId = -1L;

  String attributeName;

  String attributeValue;

  VOMSGroup group;

  public String getAttributeName() {

    return attributeName;
  }

  public void setAttributeName(String attributeName) {

    this.attributeName = attributeName;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "This field contains illegal characters!",
    expression = "^[^<>&=;]*$")
  public String getAttributeValue() {

    return attributeValue;
  }

  public void setAttributeValue(String attributeValue) {

    this.attributeValue = attributeValue;
  }

  @Action("set-attribute")
  public String setAttribute() throws Exception {

    SetRoleAttributeOperation.instance(getGroup(), getModel(),
      getAttributeName(), getAttributeValue()).execute();

    return SUCCESS;
  }

  @Action("delete-attribute")
  public String deleteAttribute() throws Exception {

    DeleteRoleAttributeOperation.instance(getGroup(), getModel(),
      getAttributeName()).execute();
    return SUCCESS;
  }

  @Override
  public void prepare() throws Exception {

    super.prepare();

    group = groupById(groupId);
  }

  public Long getGroupId() {

    return groupId;
  }

  public void setGroupId(Long groupId) {

    this.groupId = groupId;
  }

  public VOMSGroup getGroup() {

    return group;
  }

}
