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
package org.glite.security.voms.admin.view.actions.register.hr;

import static java.util.Objects.isNull;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.glite.security.voms.admin.view.actions.register.RegisterActionSupport;

public class HrRegisterActionSupport extends RegisterActionSupport implements SessionAware {

  private static final long serialVersionUID = 1L;

  private Map<String, Object> session;

  @Override
  public void prepare() throws Exception {
    super.prepare();
    requester = (RequesterInfo) session.get(ResolveHrIdAction.REQUESTER_INFO_KEY);
  }

  @Override
  public void validate() {
    if (isNull(requester)) {
      addActionError("Application information not found in session!");
    }
  }
  @Override
  public void setSession(Map<String, Object> session) {
    this.session = session;
  }

}
