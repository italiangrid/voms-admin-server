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
package org.glite.security.voms.admin.operations.roles;

import org.apache.commons.lang.xwork.Validate;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.role.RoleDeletedEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.model.VOMSRole;

public class DeleteRoleOperation extends BaseVomsOperation {

  String roleName = null;

  Long roleId;

  VOMSRole role = null;

  private DeleteRoleOperation(Long roleId) {

    this.roleId = roleId;
  }

  private DeleteRoleOperation(String roleName) {

    this.roleName = roleName;
  }

  private DeleteRoleOperation(VOMSRole r) {

    this.role = r;
  }

  protected Object doExecute() {

    VOMSRoleDAO dao = VOMSRoleDAO.instance();

    if (role == null) {

      if (roleName != null) {
        role = dao.findByName(roleName);
      } else {
        role = dao.findById(roleId);
      }
    }

    dao.delete(role);
    EventManager.instance().dispatch(new RoleDeletedEvent(role));
    return role;

  }

  public static DeleteRoleOperation instance(String name) {

    return new DeleteRoleOperation(name);
  }

  public static DeleteRoleOperation instance(VOMSRole r) {

    return new DeleteRoleOperation(r);
  }

  public static DeleteRoleOperation instance(Long id) {

    return new DeleteRoleOperation(id);
  }

  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(),
      VOMSPermission.getContainerRWPermissions());

  }
}
