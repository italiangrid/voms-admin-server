package org.glite.security.voms.admin.notification.audit;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.notification.NotificationDeliveredEvent;
import org.glite.security.voms.admin.persistence.model.notification.Notification;

public class LogNotificationDeliverySuccessTask
  extends LogNotificationDeliveyOutcomeTask {

  public LogNotificationDeliverySuccessTask(Notification notification) {
    super(notification);
  }

  @Override
  public void run() {

    EventManager.instance()
      .dispatch(new NotificationDeliveredEvent(notification));
  }

}
