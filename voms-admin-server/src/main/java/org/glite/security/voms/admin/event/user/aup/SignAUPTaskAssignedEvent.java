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

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;

import static org.glite.security.voms.admin.event.auditing.NullHelper.nullSafeValue;

public class SignAUPTaskAssignedEvent extends UserAUPEvent {

  final SignAUPTask task;

  public SignAUPTaskAssignedEvent(VOMSUser user, AUP aup, SignAUPTask task) {

    super(user, aup);
    Validate.notNull(task);
    this.task = task;

  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    super.decorateAuditEvent(e);
    e.addDataPoint("taskExpirationDate", nullSafeValue(task.getExpiryDate()));

  }
  
  
  public SignAUPTask getTask() {

    return task;
  }
}
