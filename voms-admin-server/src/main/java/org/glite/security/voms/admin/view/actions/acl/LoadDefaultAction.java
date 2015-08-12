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
import org.glite.security.voms.admin.persistence.dao.ACLDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({ @Result(name = BaseAction.SUCCESS, location = "aclDetail"),
  @Result(name = BaseAction.INPUT, location = "aclDetail") })
public class LoadDefaultAction extends ACLActionSupport {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  long aclGroupId = -1;

  @Override
  public void prepare() throws Exception {

    if (getModel() == null) {

      VOMSGroup g = groupById(aclGroupId);
      if (g.getDefaultACL() == null)
        // FIXME: do it with an operation
        model = ACLDAO.instance().create(g, true);
      else
        model = g.getDefaultACL();
    }

  }

  public long getAclGroupId() {

    return aclGroupId;
  }

  public void setAclGroupId(long aclGroupId) {

    this.aclGroupId = aclGroupId;
  }
}
