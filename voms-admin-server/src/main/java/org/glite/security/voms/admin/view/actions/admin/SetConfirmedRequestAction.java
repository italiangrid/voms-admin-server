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
package org.glite.security.voms.admin.view.actions.admin;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.TokenInterceptor;
import org.glite.security.voms.admin.operations.requests.DeleteVOMembershipRequestOperation;
import org.glite.security.voms.admin.operations.requests.SetConfimedVOMembershipRequestOperation;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "pendingRequests.jsp"),
  @Result(name = BaseAction.INPUT, location = "pendingRequests.jsp"),
  @Result(name = TokenInterceptor.INVALID_TOKEN_CODE,
    location = "pendingRequests.jsp") })
public class SetConfirmedRequestAction extends RequestActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  @Override
  public void validate() {

    if (!(request instanceof NewVOMembershipRequest))
      addActionError("Only VO membership request can be dropped with this action");

  }

  @Override
  public String execute() throws Exception {

    SetConfimedVOMembershipRequestOperation op = new SetConfimedVOMembershipRequestOperation(
      (NewVOMembershipRequest) request);
    op.execute();
    addActionMessage("Request confirmed!");
    refreshPendingRequests();
    return SUCCESS;
  }

}
