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

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.request.RoleMembershipSubmittedEvent;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.error.NoSuchRoleException;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({ @Result(name = BaseAction.SUCCESS, location = "userHome"),
  @Result(name = UserActionSupport.ERROR, location = "mappingsRequest.jsp"),
  @Result(name = UserActionSupport.INPUT, location = "prepareGroupRoleRequest") })

@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class RequestRoleMembershipAction extends UserActionSupport {

  /**
	 *
	 */
  private static final long serialVersionUID = 1L;

  Long groupId;
  Long roleId;
  String reason;

  @Override
  public void validate() {

    RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();

    VOMSGroup g = groupById(groupId);
    VOMSRole r = roleById(roleId);

    if (g == null)
      throw new NoSuchGroupException("Group with id '" + groupId
        + "' not found!");

    if (r == null)
      throw new NoSuchRoleException("Role with id '" + roleId + "' not found!");

    if (model.hasRole(g, r))
      addActionError(getText("role_request.user.already_member", new String[] {
        model.toString(), r.getName(), g.getName() }));

    if (reqDAO.userHasPendingRoleMembershipRequest(model, g, r))
      addActionError(getText("role_request.user.has_pending_request",
        new String[] { model.toString(), r.getName(), g.getName() }));

  }

  public Long getGroupId() {

    return groupId;
  }

  public void setGroupId(Long groupId) {

    this.groupId = groupId;
  }

  public Long getRoleId() {

    return roleId;
  }

  public void setRoleId(Long roleId) {

    this.roleId = roleId;
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
    VOMSRole r = roleById(roleId);

    RoleMembershipRequest request = reqDAO.createRoleMembershipRequest(model,
      reason, g, r, getDefaultFutureDate());

    EventManager.instance().dispatch(new RoleMembershipSubmittedEvent(request,
      getHomeURL()));

    refreshPendingRequests();

    return SUCCESS;
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
