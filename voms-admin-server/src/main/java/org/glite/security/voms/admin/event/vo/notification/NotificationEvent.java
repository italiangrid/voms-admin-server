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
package org.glite.security.voms.admin.event.vo.notification;

import org.glite.security.voms.admin.event.vo.VOEvent;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.persistence.model.notification.Notification;

public class NotificationEvent extends VOEvent<Notification> {

  protected NotificationEvent(Notification payload) {
    super(payload);

  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    Notification payload = getPayload();

    e.addDataPoint("notificationType", payload.getMessageType());

    int counter = 0;

    for (String recipient : payload.getRecipients()) {
      e.addDataPoint(String.format("notificationRecipient%d", counter++),
        recipient);
    }

    e.addDataPoint("notificationSubject", payload.getSubject());

    super.decorateAuditEvent(e);
  }

}
