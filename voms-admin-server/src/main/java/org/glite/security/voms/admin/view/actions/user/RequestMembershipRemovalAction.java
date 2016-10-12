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

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.request.MembershipRemovalSubmittedEvent;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({ @Result(name = BaseAction.SUCCESS, location = "userHome"),
  @Result(name = BaseAction.INPUT, location = "requestMembershipRemoval"),
  @Result(name = "registrationDisabled", location = "userHome") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class RequestMembershipRemovalAction extends UserActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  String reason;

  @Override
  public void validate() {

    VOMSUser u = CurrentAdmin.instance().getVoUser();

    if (!getModel().equals(u))
      addActionError("You cannot submit a membership removal request for another user!");

    RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();

    if (reqDAO.userHasPendingMembershipRemovalRequest(getModel()))
      addActionError("User has pending membership removal requests!");

  }

  @Override
  public String execute() throws Exception {

    if (!VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.REGISTRATION_SERVICE_ENABLED, true))
      return "registrationDisabled";

    RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();

    MembershipRemovalRequest req = reqDAO.createMembershipRemovalRequest(
      getModel(), reason, getDefaultFutureDate());

    EventManager.instance()
      .dispatch(new MembershipRemovalSubmittedEvent(req, getHomeURL()));

    refreshPendingRequests();

    return SUCCESS;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD, expression = "^[^<>&=;]*$",
    message = "You entered invalid characters in the reason field!")
  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please enter a reason.")
  public String getReason() {

    return reason;
  }

  public void setReason(String reason) {

    this.reason = reason;
  }

}
