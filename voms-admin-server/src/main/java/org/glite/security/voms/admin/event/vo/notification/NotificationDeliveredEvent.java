package org.glite.security.voms.admin.event.vo.notification;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.notification.Notification;

@EventDescription(message = "delivered a '%s' notification to '%s'",
  params = { "notificationType", "notificationRecipient0" })
public class NotificationDeliveredEvent extends NotificationEvent {

  public NotificationDeliveredEvent(Notification payload) {
    super(payload);

  }

}
