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
package org.glite.security.voms.admin.view.actions.acl;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({ @Result(name = BaseAction.SUCCESS, location = "aclManage"),
  @Result(name = BaseAction.INPUT, location = "aclManage") })
public class ManageAction extends ACLActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  Long aclGroupId = -1L;
  Long aclRoleId = -1L;

  Boolean showDefaultACL;

  @Override
  public String execute() throws Exception {

    if (getModel() == null) {

      //
    } else {

      VOMSContext ctxt = model.getContext();

      if (model.isDefautlACL())
        showDefaultACL = true;

      aclGroupId = ctxt.getGroup().getId();
      if (!ctxt.isGroupContext())
        aclRoleId = ctxt.getRole().getId();

    }

    return SUCCESS;
  }

  public Long getAclGroupId() {

    return aclGroupId;
  }

  public void setAclGroupId(Long aclGroupId) {

    this.aclGroupId = aclGroupId;
  }

  public Long getAclRoleId() {

    return aclRoleId;
  }

  public void setAclRoleId(Long aclRoleId) {

    this.aclRoleId = aclRoleId;
  }

  public Boolean getShowDefaultACL() {

    return showDefaultACL;
  }

  public void setShowDefaultACL(Boolean showDefaultACL) {

    this.showDefaultACL = showDefaultACL;
  }

}
