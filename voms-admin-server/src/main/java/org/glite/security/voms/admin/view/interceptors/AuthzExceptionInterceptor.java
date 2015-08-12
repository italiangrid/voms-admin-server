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
package org.glite.security.voms.admin.view.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.error.VOMSAuthorizationException;
import org.glite.security.voms.admin.view.actions.AuthorizationErrorAware;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor;

public class AuthzExceptionInterceptor extends ExceptionMappingInterceptor {

  public static final String ERROR_KEY = "voms-authorization-error";
  public static final Logger log = LoggerFactory
    .getLogger(AuthzExceptionInterceptor.class);
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  @Override
  public String intercept(ActionInvocation ai) throws Exception {

    String result;
    try {

      result = ai.invoke();
      return result;

    } catch (Exception e) {

      if (e instanceof VOMSAuthorizationException) {

        log.debug("Caught VOMS authorization exception: " + e);

        if (ai.getAction() instanceof ValidationAware) {

          ValidationAware vaAction = (ValidationAware) ai.getAction();
          vaAction.addActionError(e.getMessage());

        }

        if (ai.getAction() instanceof AuthorizationErrorAware) {

          AuthorizationErrorAware aeaAction = (AuthorizationErrorAware) ai
            .getAction();
          return aeaAction.getAuthorizationErrorResult();

        }
        return BaseAction.INPUT;
      }

      // Fall back to standard exception mapping mechanism for other
      // exceptions
      throw e;
    }

  }
}

class AuthzExceptionHolder {

  VOMSAuthorizationException authorizationException;

  public AuthzExceptionHolder(VOMSAuthorizationException e) {

    authorizationException = e;
  }

  public VOMSAuthorizationException getAuthorizationException() {

    return authorizationException;
  }

}
