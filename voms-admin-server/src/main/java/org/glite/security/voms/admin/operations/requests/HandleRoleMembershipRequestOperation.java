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

package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.request.RoleMembershipApprovedEvent;
import org.glite.security.voms.admin.event.request.RoleMembershipRejectedEvent;
import org.glite.security.voms.admin.event.user.membership.RoleAssignedEvent;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.users.AssignRoleOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;

public class HandleRoleMembershipRequestOperation extends
  GroupManagerRoleHolderOperation<RoleMembershipRequest> {

  public HandleRoleMembershipRequestOperation(RoleMembershipRequest request,
    DECISION decision) {

    super(request, decision);
  }

  @Override
  protected void approve() {

    checkRequestStatus(STATUS.SUBMITTED);

    VOMSUser u = getRequesterAsVomsUser();
    VOMSGroup g = findGroupByName(request.getGroupName());
    VOMSRole r = findRoleByName(request.getRoleName());

    VOMSUserDAO.instance().assignRole(u, g, r);

    approveRequest();

    EventManager.instance().dispatch(new RoleAssignedEvent(u,g,r));
    EventManager.instance().dispatch(new RoleMembershipApprovedEvent(request));

  }

  @Override
  protected void reject() {

    checkRequestStatus(STATUS.SUBMITTED);

    rejectRequest();
    EventManager.instance().dispatch(new RoleMembershipRejectedEvent(request));

  }

  @Override
  protected void setupPermissions() {

    VOMSGroup g = findGroupByName(request.getGroupName());
    VOMSRole r = findRoleByName(request.getRoleName());

    addRequiredPermissionOnPath(g, VOMSPermission.getContainerReadPermission());

    addRequiredPermission(VOMSContext.instance(g, r),
      VOMSPermission.getMembershipRWPermissions());
    addRequiredPermission(VOMSContext.instance(g, r),
      VOMSPermission.getRequestsRWPermissions());

  }

}
