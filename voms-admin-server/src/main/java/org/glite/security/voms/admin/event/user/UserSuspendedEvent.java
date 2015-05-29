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
package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.event.EventDescription;

import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

@EventDescription(message = "suspended user '%s %s' for reason '%s'",
  params = { "userName", "userSurname", "suspensionReason" })
public class UserSuspendedEvent extends UserLifecycleEvent {

  final SuspensionReason reason;

  public UserSuspendedEvent(VOMSUser user, SuspensionReason reason) {

    super(user);
    this.reason = reason;

  }

  public SuspensionReason getReason() {

    return reason;
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    super.decorateAuditEvent(e);
    e.addDataPoint("suspensionReason", getReason().getMessage());
  }

}
