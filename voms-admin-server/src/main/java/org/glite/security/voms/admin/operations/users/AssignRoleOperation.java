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
package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.membership.RoleAssignedEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.roles.FindRoleOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.error.NoSuchRoleException;
import org.glite.security.voms.admin.persistence.error.NoSuchUserException;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class AssignRoleOperation extends BaseVomsOperation {

  VOMSUser user;

  VOMSGroup group;

  VOMSRole role;

  private AssignRoleOperation(Long userId, String roleFqan) {

    user = (VOMSUser) FindUserOperation.instance(userId).execute();

    VOMSContext context = VOMSContext.instance(roleFqan);

    group = context.getGroup();
    role = context.getRole();

  }

  private AssignRoleOperation(VOMSUser u, VOMSGroup g, VOMSRole r) {

    user = u;
    group = g;
    role = r;
  }

  public Object doExecute() {

    VOMSUserDAO.instance().assignRole(user, group, role);

    EventManager.instance().dispatch(new RoleAssignedEvent(user, group, role));

    return user;
  }

  public static AssignRoleOperation instance(Long userId, String roleFqan) {

    return new AssignRoleOperation(userId, roleFqan);
  }

  public static AssignRoleOperation instance(VOMSUser u, VOMSGroup g, VOMSRole r) {

    return new AssignRoleOperation(u, g, r);
  }

  public static AssignRoleOperation instance(String groupName, String roleName,
    String userName, String caDn) {

    VOMSUser u = (VOMSUser) FindUserOperation.instance(userName, caDn)
      .execute();
    VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupName).execute();
    VOMSRole r = (VOMSRole) FindRoleOperation.instance(roleName).execute();

    if (u == null)
      throw new NoSuchUserException("User '" + userName + "," + caDn
        + "' not found in database.");
    if (g == null)
      throw new NoSuchGroupException("Group '" + groupName
        + "' not found in database.");
    if (r == null)
      throw new NoSuchRoleException("Role '" + roleName
        + "' not found in database.");

    return new AssignRoleOperation(u, g, r);

  }

  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.instance(group), VOMSPermission
      .getContainerReadPermission().setMembershipRWPermission());
  }

}
