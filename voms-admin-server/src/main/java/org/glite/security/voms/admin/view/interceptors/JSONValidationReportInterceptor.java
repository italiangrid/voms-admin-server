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
package org.glite.security.voms.admin.view.interceptors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class JSONValidationReportInterceptor extends MethodFilterInterceptor {

  public static final Logger log = LoggerFactory
    .getLogger(JSONValidationReportInterceptor.class);

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  private int validationFailedStatus = -1;

  /**
   * HTTP status that will be set in the response if validation fails
   * 
   * @param validationFailedStatus
   */
  public void setValidationFailedStatus(int validationFailedStatus) {

    this.validationFailedStatus = validationFailedStatus;
  }

  @Override
  protected String doIntercept(ActionInvocation invocation) throws Exception {

    // Pre action invocation validation
    if (hasValidationErrors(invocation)) {
      return generateJSON((ValidationAware) invocation.getAction(), null);
    }

    String result = invocation.invoke();

    if (hasValidationErrors(invocation)) {
      return generateJSON((ValidationAware) invocation.getAction(), null);
    }

    return result;

  }

  protected boolean hasValidationErrors(ActionInvocation invocation)
    throws IOException, JSONException {

    Object action = invocation.getAction();
    if (action instanceof ValidationAware) {

      ValidationAware validationAware = (ValidationAware) action;
      return validationAware.hasErrors();

    }
    return false;
  }

  protected String generateJSON(ValidationAware validationAware, Throwable t)
    throws IOException, JSONException {
    return generateJSON(validationAware, t, -1);
  }
  
  protected String generateJSON(ValidationAware validationAware, Throwable t, int statusCode)
    throws IOException, JSONException {

    HttpServletResponse response = ServletActionContext.getResponse();
    
    response.setContentType("application/json");
    
    if (statusCode > 0){
      
      response.sendError(statusCode, t.getMessage());      
    
    } else {
      if (validationFailedStatus >= 0) {
        response.setStatus(validationFailedStatus);
      }
      String responseContent = buildResponse(validationAware, t);
      
      response.getWriter().print(responseContent);
    }    
    
    return Action.NONE;
  }

  protected String buildResponse(ValidationAware validationAware, Throwable t)
    throws JSONException {

    Map<String, Object> errors = new HashMap<String, Object>();

    if (t != null) {
      errors.put("exceptionClass", t.getClass().getName());
      errors.put("exceptionMessage", t.getMessage());
    }

    if (validationAware.hasFieldErrors())
      errors.put("fieldErrors", validationAware.getFieldErrors());

    if (validationAware.hasActionErrors())
      errors.put("errors", validationAware.getActionErrors());

    if (validationAware.hasActionMessages())
      errors.put("messages", validationAware.getActionMessages());

    return JSONUtil.serialize(errors);

  }
}
