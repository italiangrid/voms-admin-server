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
package org.glite.security.voms.admin.operations.aup;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.aup.AUPChangedPeriodEvent;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.AUP;

public class ChangeReacceptancePeriodOperation extends BaseVomsOperation {

  AUP aup;
  int period;

  public ChangeReacceptancePeriodOperation(AUP aup, int period) {

    this.aup = aup;
    this.period = period;

  }

  @Override
  protected Object doExecute() {

    aup.setReacceptancePeriod(period);
    
    EventManager.instance().dispatch(new AUPChangedPeriodEvent(aup));
    
    return aup;
  }

  public AUP getAup() {

    return aup;
  }

  public void setAup(AUP aup) {

    this.aup = aup;
  }

  public int getPeriod() {

    return period;
  }

  public void setPeriod(int period) {

    this.period = period;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerRWPermissions().setMembershipRWPermission());
  }

}
