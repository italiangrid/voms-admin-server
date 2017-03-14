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

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.UserOrgDbIdUpdatedEvent;
import org.glite.security.voms.admin.operations.BaseVoRWOperation;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class SetUserOrgDbIdOperation extends BaseVoRWOperation {

  private VOMSUser user;
  private Long orgDbId;

  public SetUserOrgDbIdOperation(VOMSUser user, Long orgDbId) {

    this.user = user;
    this.orgDbId = orgDbId;
  }

  @Override
  protected Object doExecute() {

    user.setOrgDbId(orgDbId);
    
    EventManager.instance().dispatch(new UserOrgDbIdUpdatedEvent(user));
    return user;
  }
  
}
