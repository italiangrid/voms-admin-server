/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

package org.glite.security.voms.admin.view.actions.register;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.request.VOMembershipRequestConfirmedEvent;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;
import org.glite.security.voms.admin.util.URLBuilder;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({
  @Result(name = BaseAction.SUCCESS, location = "registrationConfirmed"),
  @Result(name = BaseAction.ERROR, location = "registrationConfirmationError") })
public class SetRequestConfirmedAction extends RegisterActionSupport {

  private static final long serialVersionUID = -3732391685550459780L;

  String confirmationId;

  @Override
  public String execute() throws Exception {

    if (!registrationEnabled())
      return REGISTRATION_DISABLED;

    if (!getModel().getStatus().equals(STATUS.SUBMITTED)) {
      addActionError("Your request has already been confirmed!");
      return ERROR;
    }

    if (!getModel().getConfirmId().equals(confirmationId)) {
      addActionError("Wrong confirmation id!");
      return ERROR;
    }

    getModel().setStatus(STATUS.CONFIRMED);

    EventManager.dispatch(new VOMembershipRequestConfirmedEvent(request,
      URLBuilder.buildLoginURL()));

    return SUCCESS;
  }

  /**
   * @return the confirmationId
   */
  public String getConfirmationId() {

    return confirmationId;
  }

  /**
   * @param confirmationId
   *          the confirmationId to set
   */
  public void setConfirmationId(String confirmationId) {

    this.confirmationId = confirmationId;
  }

}
