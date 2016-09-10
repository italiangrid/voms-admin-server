package org.glite.security.voms.admin.notification.audit;

import org.glite.security.voms.admin.persistence.model.notification.Notification;

public abstract class LogNotificationDeliveyOutcomeTask implements Runnable {

  protected final Notification notification;

  public LogNotificationDeliveyOutcomeTask(Notification notification) {
    this.notification = notification;
  }

}
