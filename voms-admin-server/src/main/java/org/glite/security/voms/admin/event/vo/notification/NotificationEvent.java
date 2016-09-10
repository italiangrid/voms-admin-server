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
