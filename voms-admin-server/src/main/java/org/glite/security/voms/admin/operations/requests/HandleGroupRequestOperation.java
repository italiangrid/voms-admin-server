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
package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.request.GroupMembershipApprovedEvent;
import org.glite.security.voms.admin.event.request.GroupMembershipRejectedEvent;
import org.glite.security.voms.admin.event.user.membership.UserAddedToGroupEvent;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.Request.STATUS;

public class HandleGroupRequestOperation extends
  GroupManagerRoleHolderOperation<GroupMembershipRequest>{

  public HandleGroupRequestOperation(GroupMembershipRequest request,
    DECISION decision) {

    super(request, decision);
  }
  
  private void addUserToGroupRecursive(VOMSUser u, VOMSGroup g){
    
    if (!g.isRootGroup()){
      if (!u.isMember(g.getParent())){
        addUserToGroupRecursive(u, g.getParent());
      }
    }
    
    VOMSUserDAO.instance().addToGroup(u, g);
    EventManager.instance().dispatch(new UserAddedToGroupEvent(u,g));
  }

  @Override
  protected void approve() {

    checkRequestStatus(STATUS.SUBMITTED);

    VOMSUser u = getRequesterAsVomsUser();
    VOMSGroup g = findGroupByName(request.getGroupName());

    if (!u.isMember(g)){
      addUserToGroupRecursive(u, g);
    }

    approveRequest();

    EventManager.instance().dispatch(new GroupMembershipApprovedEvent(request));

  }

  @Override
  protected void reject() {

    checkRequestStatus(STATUS.SUBMITTED);
    rejectRequest();
    EventManager.instance().dispatch(new GroupMembershipRejectedEvent(request));
  }

  @Override
  protected void setupPermissions() {

    VOMSGroup g = findGroupByName(request.getGroupName());
    addRequiredPermissionOnPath(g, VOMSPermission.getContainerReadPermission());
    addRequiredPermission(VOMSContext.instance(g),
      VOMSPermission.getMembershipRWPermissions());
    addRequiredPermission(VOMSContext.instance(g),
      VOMSPermission.getRequestsRWPermissions());

  }

}
