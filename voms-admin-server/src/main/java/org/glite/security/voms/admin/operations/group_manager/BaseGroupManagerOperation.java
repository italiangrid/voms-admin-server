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
package org.glite.security.voms.admin.operations.group_manager;

import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.GroupManagerDAO;
import org.glite.security.voms.admin.persistence.model.GroupManager;

public abstract class BaseGroupManagerOperation extends BaseVomsOperation {

  protected GroupManager manager;
  protected GroupManagerDAO dao;

  protected BaseGroupManagerOperation(GroupManager m) {

    manager = m;
    dao = DAOFactory.instance().getGroupManagerDAO();
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(),
      VOMSPermission.getAllPermissions());

  }

  /**
   * @return the manager
   */
  public GroupManager getManager() {

    return manager;
  }

  /**
   * @param manager
   *          the manager to set
   */
  public void setManager(GroupManager manager) {

    this.manager = manager;
  }

}
