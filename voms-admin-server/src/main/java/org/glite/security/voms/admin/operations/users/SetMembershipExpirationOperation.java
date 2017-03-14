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
package org.glite.security.voms.admin.operations.users;

import java.util.Date;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.UserMembershipExpirationDateUpdated;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class SetMembershipExpirationOperation extends BaseVomsOperation {

  VOMSUser user;
  Date expirationDate;

  public SetMembershipExpirationOperation(VOMSUser u, Date d) {

    this.user = u;
    this.expirationDate = d;
  }

  @Override
  protected Object doExecute() {

    user.setEndTime(expirationDate);
    EventManager.instance().dispatch(new UserMembershipExpirationDateUpdated(user));
    
    return user;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerRWPermissions().setMembershipRWPermission());

  }

  public static SetMembershipExpirationOperation instance(Long userId,
    Date expirationDate) {

    VOMSUser user = VOMSUserDAO.instance().findById(userId);
    return new SetMembershipExpirationOperation(user, expirationDate);

  }

}
