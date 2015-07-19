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
package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.request.GroupMembershipSubmittedEvent;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({

@Result(name = UserActionSupport.SUCCESS, location = "userHome"),
  @Result(name = UserActionSupport.ERROR, location = "mappingsRequest.jsp"),
  @Result(name = UserActionSupport.INPUT, location = "prepareGroupRequest") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class RequestGroupMembershipAction extends UserActionSupport {

  /**
	 *
	 */
  private static final long serialVersionUID = 1L;
  Long groupId;
  String reason;

  @Override
  public void validate() {

    RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();

    VOMSGroup g = groupById(groupId);

    if (g == null)
      throw new NoSuchGroupException("Group with id '" + groupId
        + "' not found!");

    if (model.isMember(g))
      addActionError(getText("group_request.user.already_member", new String[] {
        model.toString(), g.getName() }));

    if (reqDAO.userHasPendingGroupMembershipRequest(model, g))
      addActionError(getText("group_request.user.has_pending_request",
        new String[] { model.toString(), g.getName() }));
  }

  @Override
  public String execute() throws Exception {

    if (!VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.REGISTRATION_SERVICE_ENABLED, true))
      return "registrationDisabled";

    if (hasActionErrors())
      return ERROR;

    RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
    VOMSGroup g = groupById(groupId);

    GroupMembershipRequest req = reqDAO.createGroupMembershipRequest(
      getModel(), reason, g, getDefaultFutureDate());

    EventManager.instance().dispatch(new GroupMembershipSubmittedEvent(req, getHomeURL()));

    refreshPendingRequests();

    return SUCCESS;
  }

  public Long getGroupId() {

    return groupId;
  }

  public void setGroupId(Long groupId) {

    this.groupId = groupId;
  }

  @RegexFieldValidator(type = ValidatorType.FIELD, expression = "^[^<>&=;]*$",
          message = "You entered invalid characters in the reason field!")
  @RequiredStringValidator(type = ValidatorType.FIELD,
          message = "Please enter a reason.")
  @StringLengthFieldValidator(
    minLength = "1",
    maxLength = "255",
    message = "Your reason must have from ${minLength} to ${maxLength} characters"
    )
  public String getReason() {

    return reason;
  }

  public void setReason(String reason) {

    this.reason = reason;
  }
}
