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
package org.glite.security.voms.admin.view.actions.admin;

import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.apache.tiles.context.MapEntry;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({

  @Result(name = BaseAction.SUCCESS, location = "adminHome"),
  @Result(name = BaseAction.INPUT, location = "adminHome"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE,
  location = "adminHome")
  
})

@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute"})
public class BulkRejectAction extends BulkDecisionAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  String motivation;

  @Override
  public void prepare() throws Exception {
    decision = "reject";
    super.prepare();
  }
  
  @Override
  public void validate() {
    if (hasFieldErrors()){
      Map<String, List<String>> fieldErrors = getFieldErrors();
      for (Map.Entry<String, List<String>> e: fieldErrors.entrySet()){
        addActionError("Invalid motivation");
      }
    }
    super.validate();
  }

  public String getMotivation() {

    return motivation;
  }
  
  @Override
  public String execute() throws Exception {
  
    for (Request r: selectedRequests){
      r.setExplanation(motivation);
    }
    
    return super.execute();
  }

  @RequiredStringValidator
  @RegexFieldValidator(type = ValidatorType.FIELD, expression = "^[^<>&=;]*$",
    message = "You entered invalid characters in the suspension reason field!")
  public void setMotivation(String motivation) {

    this.motivation = motivation;
  }

}
