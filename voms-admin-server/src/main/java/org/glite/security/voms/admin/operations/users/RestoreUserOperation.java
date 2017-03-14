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

import org.glite.security.voms.admin.core.validation.ValidationUtil;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.UserRestoredEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestoreUserOperation extends BaseVomsOperation {

  public static final Logger log = LoggerFactory
    .getLogger(RestoreUserOperation.class);

  VOMSUser user;

  protected RestoreUserOperation(Long userId) {

    user = VOMSUserDAO.instance().findById(userId);
  }

  protected RestoreUserOperation(VOMSUser u) {

    user = u;
  }

  public static RestoreUserOperation instance(Long id) {

    return new RestoreUserOperation(id);
  }

  public static RestoreUserOperation instance(VOMSUser u) {

    return new RestoreUserOperation(u);
  }

  @Override
  protected Object doExecute() {

    log.info("Restoring user {}.", user.getShortName());

    if (user.isSuspended()) {

      if (user.getSuspensionReason().equals(
        SuspensionReason.FAILED_TO_SIGN_AUP.getMessage())) {
        AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();

        // Sign AUP on behalf of the user
        VOMSUserDAO.instance().signAUP(user, aupDAO.getVOAUP());
        log.debug("Creating AUP acceptance record for user {}", user);

      }

      if (user.hasExpired()) {
        // Extend membership
        Date expirationDate = ValidationUtil
          .membershipExpirationDateStartingFromNow();
        log.debug("Extending membership for user {} to {}", user,
          expirationDate);
        user.setEndTime(expirationDate);
      }

      user.restore();

      EventManager.instance().dispatch(new UserRestoredEvent(user));

    }
    
    return user;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerReadPermission().setMembershipReadPermission()
      .setSuspendPermission());
  }

}
