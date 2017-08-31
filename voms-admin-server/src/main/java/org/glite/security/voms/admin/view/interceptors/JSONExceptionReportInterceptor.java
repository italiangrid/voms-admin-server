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

import org.glite.security.voms.admin.error.VOMSAuthorizationException;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;

public class JSONExceptionReportInterceptor extends JSONValidationReportInterceptor {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  protected String doIntercept(ActionInvocation invocation) throws Exception {

    try {

      String result = invocation.invoke();

      return result;

    } catch (VOMSAuthorizationException e) {
      log.debug("Authorization error {} while executing action {}", e.getMessage(),
          invocation.getAction());
      return generateJSON((ValidationAware) invocation.getAction(), e);

    } catch (Throwable t) {

      log.debug("Caught {} while executing action {}", t, invocation.getAction());
      log.debug(t.getMessage(), t);

      return generateJSON((ValidationAware) invocation.getAction(), t, 500);

    }
  }
}
