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

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({ @Result(name = BaseAction.SUCCESS, location = "userHome"),
  @Result(name = BaseAction.INPUT, location = "userHome") })
public class HomeAction extends UserActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public void prepare() throws Exception {

    super.prepare();
    
    if (CurrentAdmin.instance()
      .getVoUser() == null) {
      addActionError(
        "Current authenticated client has no linked VO membership!");
    }

    model = CurrentAdmin.instance()
      .getVoUser();

    if (model != null) {
      refreshPendingRequests();

    }

  }

  public boolean currentAdminIsCertificate(Certificate c) {

    return CurrentAdmin.instance()
      .is(c);
  }

}
