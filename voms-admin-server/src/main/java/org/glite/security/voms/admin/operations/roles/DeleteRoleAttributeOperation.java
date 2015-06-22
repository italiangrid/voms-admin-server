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

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.role.RoleAttributeDeletedEvent;
import org.glite.security.voms.admin.operations.BaseAttributeRWOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.error.NoSuchRoleException;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSRoleAttribute;

public class DeleteRoleAttributeOperation extends BaseAttributeRWOperation {

  String attributeName;

  protected Object doExecute() {

    VOMSRoleAttribute ra = VOMSRoleDAO.instance().deleteAttributeByName(
      __context.getRole(), __context.getGroup(), attributeName);

    EventManager.instance()
      .dispatch(new RoleAttributeDeletedEvent(__context.getRole(), ra));

    return ra;
  }

  private DeleteRoleAttributeOperation(VOMSGroup g, VOMSRole r,
    String attributeName) {

    super(VOMSContext.instance(g, r));
    this.attributeName = attributeName;
  }

  public static DeleteRoleAttributeOperation instance(VOMSGroup g, VOMSRole r,
    String attributeName) {

    return new DeleteRoleAttributeOperation(g, r, attributeName);
  }

  public static DeleteRoleAttributeOperation instance(String groupName,
    String roleName, String attributeName) {

    VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName).execute();
    VOMSRole r = (VOMSRole) FindRoleOperation.instance(roleName).execute();

    if (g == null)
      throw new NoSuchGroupException("Group '" + groupName + "' not found!");

    if (r == null)
      throw new NoSuchRoleException("Role '" + roleName + "' not found!");

    return new DeleteRoleAttributeOperation(g, r, attributeName);
  }

}
