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
package org.glite.security.voms.admin.event.user.aup;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;

@EventDescription(message="canceled sign aup task for user %s,%s: %s",
params = { "userName", "userSurname", "taskCancellationReason" })
public class SignAUPTaskCanceledEvent extends UserAUPTaskEvent{
  
  private final String cancellationReason;
  
  public SignAUPTaskCanceledEvent(SignAUPTask task, String cancellationReason) {
   super(task.getUser(), task.getAup(), task);
   this.cancellationReason = cancellationReason;
  }
  
  @Override
  protected void decorateAuditEvent(AuditEvent e) {
    super.decorateAuditEvent(e);
    e.addDataPoint("taskCancellationReason", cancellationReason);
  }
}
