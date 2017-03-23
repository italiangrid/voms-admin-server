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
package org.glite.security.voms.admin.view.actions.user;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.users.DeleteUserAttributeOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSAttributeDescription;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "attributes.jsp"),
  @Result(name = BaseAction.INPUT, location = "attributes.jsp"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE,
    location = "attributes.jsp") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "deleteAttribute" })
public class AttributeActions extends UserActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  String attributeName;

  String attributeValue;

  List<VOMSAttributeDescription> attributeClasses;

  @Action("delete-attribute")
  public String deleteAttribute() throws Exception {

    DeleteUserAttributeOperation.instance(getModel(), attributeName).execute();
    addActionMessage(getText("confirm.user.attribute_delete", new String[] {
      attributeName, getModel().getShortName() }));
    return SUCCESS;
  }

  public String getAttributeName() {

    return attributeName;
  }

  public void setAttributeName(String attributeName) {

    this.attributeName = attributeName;
  }

  @StringLengthFieldValidator(type = ValidatorType.FIELD,
    message = "The value for this attribute is too long", maxLength = "255")
  // @RegexFieldValidator(type = ValidatorType.FIELD, message =
  // "This field contains illegal characters!", regex = "^[^<>&=;]*$")
  public String getAttributeValue() {

    return attributeValue;
  }

  public void setAttributeValue(String attributeValue) {

    this.attributeValue = attributeValue;
  }

  public List<VOMSAttributeDescription> getAttributeClasses() {

    return attributeClasses;
  }

  public void setAttributeClasses(
    List<VOMSAttributeDescription> attributeClasses) {

    this.attributeClasses = attributeClasses;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void prepare() throws Exception {

    super.prepare();
    attributeClasses = VOMSAttributeDAO.instance()
      .getAllAttributeDescriptions();

  }
}
