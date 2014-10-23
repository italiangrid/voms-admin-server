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

package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;

public class ConditionalRestoreUserOperation extends RestoreUserOperation {

  private SuspensionReason suspensionReason;

  private ConditionalRestoreUserOperation(VOMSUser u,
    SuspensionReason suspensionReason) {

    super(u);
    this.suspensionReason = suspensionReason;
  }

  private ConditionalRestoreUserOperation(Long userId,
    SuspensionReason suspensionReason) {

    super(userId);
    this.suspensionReason = suspensionReason;
  }

  @Override
  protected Object doExecute() {

    boolean userHasBeenRestored = false;

    if (user.isSuspended()
      && user.getSuspensionReason().equals(suspensionReason)) {
      user.restore();
      userHasBeenRestored = true;
    }

    return userHasBeenRestored;
  }

  public static ConditionalRestoreUserOperation instance(Long userId,
    SuspensionReason reason) {

    return new ConditionalRestoreUserOperation(userId, reason);

  }

  public static ConditionalRestoreUserOperation instance(VOMSUser u,
    SuspensionReason reason) {

    return new ConditionalRestoreUserOperation(u, reason);

  }
}
