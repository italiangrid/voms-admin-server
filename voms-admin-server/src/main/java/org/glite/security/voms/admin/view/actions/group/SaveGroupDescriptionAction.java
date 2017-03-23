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

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.groups.SetGroupDescriptionOperation;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "groupDetail"),
  @Result(name = BaseAction.INPUT, location = "groupEditDescription"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE,
    location = "groupEditDescription")

})
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class SaveGroupDescriptionAction extends GroupActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  String groupDescription;

  
  @Override
  public String execute() throws Exception {
    
    if ("".equals(groupDescription.trim()))
      groupDescription = null;
    
    group = (VOMSGroup) SetGroupDescriptionOperation.instance(group, groupDescription).execute();

    addActionMessage(getText("confirm.group.description.edited", group.getName()));
        
    return SUCCESS;
  }

  
  public String getGroupDescription() {

    return groupDescription;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD,
    message = "The description field contains illegal characters!",
    regex = "^[^<>&=;]*$")
  @StringLengthFieldValidator(type = ValidatorType.FIELD, maxLength = "255",
    message = "The description field size is limited to 255 characters.")
  public void setGroupDescription(String description) {

    this.groupDescription = description;
  }

}
