package org.glite.security.voms.admin.event.vo.notification;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.notification.messages.VOMSNotification;

@EventDescription(message = "could not deliver notification '%s' to '%s'",
params = { "notificationType",  "notificationRecipient0" })
public class NotificationDeliveryErrorEvent extends NotificationEvent {

  public NotificationDeliveryErrorEvent(VOMSNotification payload) {
    super(payload);
    
  }

}
