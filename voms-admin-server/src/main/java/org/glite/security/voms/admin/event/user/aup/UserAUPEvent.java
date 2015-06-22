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
package org.glite.security.voms.admin.event.user.aup;

import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.user.UserEvent;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class UserAUPEvent extends UserEvent {

  final AUP aup;

  public UserAUPEvent(VOMSUser user, AUP aup) {

    super(EventCategory.UserAUPEvent, user);
    this.aup = aup;

  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {
  
    super.decorateAuditEvent(e);
    e.addDataPoint("aupName", aup.getName());
    e.addDataPoint("aupVersion", aup.getActiveVersion().getVersion());
    
  }
  
  
  public AUP getAup() {

    return aup;
  }

}
