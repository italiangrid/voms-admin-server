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

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.users.SetUserAttributeOperation;
import org.glite.security.voms.admin.persistence.error.AttributeValueAlreadyAssignedException;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({ @Result(name = BaseAction.SUCCESS, location = "attributes.jsp"),
  @Result(name = BaseAction.INPUT, location = "attributes.jsp") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class SetAttributeAction extends AttributeActions {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  @Override
  public String execute() throws Exception {

    try {
      SetUserAttributeOperation.instance(getModel(), attributeName,
        attributeValue).execute();

    } catch (AttributeValueAlreadyAssignedException e) {
      addFieldError("attributeValue", "The attribute value '" + attributeValue
        + "' is already assigned to another user!");
      return INPUT;
    }

    addActionMessage(getText("confirm.user.attribute_set", new String[] {
      attributeName, getModel().getShortName() }));

    return SUCCESS;
  }

}
