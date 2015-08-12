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
package org.glite.security.voms.admin.operations.aup;

import java.util.Date;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.aup.UserAUPSignatureRequestedEvent;
import org.glite.security.voms.admin.event.vo.aup.TriggeredForcedReacceptanceEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPAcceptanceRecord;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class TriggerReacceptanceOperation extends BaseVomsOperation {

  AUP aup;
  VOMSUser user;

  public TriggerReacceptanceOperation(AUP a, VOMSUser u) {

    this.aup = a;
    this.user = u;
  }

  public TriggerReacceptanceOperation(AUP a) {

    this.aup = a;
    this.user = null;
  }

  @Override
  protected Object doExecute() {

    if (user == null) {
      aup.getActiveVersion().setLastForcedReacceptanceTime(new Date());

      EventManager.instance().dispatch(new TriggeredForcedReacceptanceEvent(aup));

    } else {
      AUPAcceptanceRecord record = user.getAUPAccceptanceRecord(aup
        .getActiveVersion());

      if (record != null && !record.hasExpired())
        record.setValid(false);

      EventManager.instance().dispatch(new UserAUPSignatureRequestedEvent(user, aup));
    }

    return aup;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerRWPermissions().setMembershipRWPermission()
      .setSuspendPermission());

  }

}
