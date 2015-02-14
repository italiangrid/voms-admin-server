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

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.GroupManager;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.INPUT, location = "selectManager"),
  @Result(name = RegisterActionSupport.REQUEST_ATTRIBUTES,
    location = "requestAttributes"),
  @Result(name = BaseAction.SUCCESS, location = "set-request-confirmed",
    type = "chain"),
  @Result(name = BaseAction.ERROR, location = "registrationConfirmationError") })
public class SelectManagerAction extends RegisterActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = -7576052921720818553L;

  Long managerId = -1L;
  String confirmationId;
  GroupManager manager;

  @Override
  public void validate() {

    if (managerId != -1L) {
      manager = DAOFactory.instance().getGroupManagerDAO()
        .findById(managerId, false);

      if (manager == null) {
        addActionError("No manager found for id '" + managerId + "'.");
      }

    } else {

      if (requireGroupManagerSelection()) {
        
        addActionError("Please select a group manager.");
      
      }

    }

    super.validate();
  }

  @Override
  public String execute() throws Exception {

    if (!getModel().getStatus().equals(STATUS.SUBMITTED)) {
      addActionError("Your request has already been confirmed!");
      return ERROR;
    }

    if (manager != null) {

      getModel().getRequesterInfo().setManagerEmail(manager.getEmailAddress());

    }

    List<VOMSGroup> groups = VOMSGroupDAO.instance().getAll();
    if (attributeRequestsEnabled() && groups.size() > 1) {

      // All members are included in the root group by default, so
      // the root group is removed from the list of groups that could be
      // requested
      if (groups.size() > 1)
        groups = groups.subList(1, groups.size());

      ServletActionContext.getRequest().setAttribute("voGroups", groups);
      return REQUEST_ATTRIBUTES;
    }

    return SUCCESS;
  }

  @RequiredFieldValidator(type = ValidatorType.FIELD,
    message = "A confirmation id is required!")
  public String getConfirmationId() {

    return confirmationId;
  }

  public void setConfirmationId(String confirmationId) {

    this.confirmationId = confirmationId;
  }

  /**
   * @return the managerId
   */
  public Long getManagerId() {

    return managerId;
  }

  /**
   * @param managerId
   *          the managerId to set
   */
  public void setManagerId(Long managerId) {

    this.managerId = managerId;
  }

}
