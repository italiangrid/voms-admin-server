package org.glite.security.voms.admin.event.vo.notification;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.notification.messages.VOMSNotification;

@EventDescription(message = "delivered a '%s' notification to '%s'",
params = { "notificationType",  "notificationRecipient0" })
public class NotificationDeliveredEvent extends NotificationEvent {

  public NotificationDeliveredEvent(VOMSNotification payload) {
    super(payload);
    
  }

}
