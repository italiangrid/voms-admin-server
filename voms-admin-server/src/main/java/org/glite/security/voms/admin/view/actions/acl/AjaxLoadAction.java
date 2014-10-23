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
package org.glite.security.voms.admin.view.actions.acl;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({ @Result(name = BaseAction.SUCCESS, location = "aclDetail.jsp"),
  @Result(name = BaseAction.INPUT, location = "aclManage.jsp") })
public class AjaxLoadAction extends ACLActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  Long aclGroupId = -1L;
  Long aclRoleId = -1L;

  Boolean showDefaultACL;

  VOMSContext vomsContext;

  @SkipValidation
  public String execute() throws Exception {

    return SUCCESS;
  }

  @Override
  public void prepare() throws Exception {

    vomsContext = null;

    if (getModel() == null) {
      if (aclGroupId != -1L && aclRoleId != -1L)
        vomsContext = VOMSContext.instance(aclGroupId, aclRoleId);
      else if (aclGroupId != -1L)
        vomsContext = VOMSContext.instance(groupById(aclGroupId));

      if (vomsContext != null) {

        if (showDefaultACL != null && vomsContext.isGroupContext())
          model = vomsContext.getGroup().getDefaultACL();
        else
          model = vomsContext.getACL();
      }

    }

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

  public VOMSContext getVomsContext() {

    return vomsContext;
  }

  public void setVomsContext(VOMSContext vomsContext) {

    this.vomsContext = vomsContext;
  }

}
