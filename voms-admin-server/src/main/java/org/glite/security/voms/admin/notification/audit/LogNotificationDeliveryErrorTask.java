package org.glite.security.voms.admin.notification.audit;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.notification.NotificationDeliveryErrorEvent;
import org.glite.security.voms.admin.persistence.model.notification.Notification;

public class LogNotificationDeliveryErrorTask
  extends LogNotificationDeliveyOutcomeTask {

  public LogNotificationDeliveryErrorTask(Notification notification) {
    super(notification);
  }

  @Override
  public void run() {

    EventManager.instance()
      .dispatch(new NotificationDeliveryErrorEvent(notification));
  }

}
