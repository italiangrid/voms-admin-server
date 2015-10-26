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

import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;

public class SuspendUserOperation extends BaseVomsOperation {

  VOMSUser user;

  SuspensionReason reason;

  private SuspendUserOperation(Long userId, String r) {

    this.user = VOMSUserDAO.instance().findById(userId);

    this.reason = SuspensionReason.OTHER;
    this.reason.setMessage(r);

  }

  private SuspendUserOperation(VOMSUser u, SuspensionReason r) {

    this.user = u;
    reason = r;

  }

  public static SuspendUserOperation instance(Long userId, String r) {

    return new SuspendUserOperation(userId, r);
  }

  public static SuspendUserOperation instance(VOMSUser u, SuspensionReason r) {

    return new SuspendUserOperation(u, r);
  }

  @Override
  protected Object doExecute() {

    if (user == null)
      throw new NullArgumentException("User cannot be null!");
    if (reason == null)
      throw new NullArgumentException("Reason cannot be null!");

    ValidationManager.instance().suspendUser(user, reason);

    return null;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerReadPermission().setMembershipReadPermission()
      .setSuspendPermission());

  }

}
