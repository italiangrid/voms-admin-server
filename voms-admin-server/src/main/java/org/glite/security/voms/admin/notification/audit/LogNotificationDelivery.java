package org.glite.security.voms.admin.notification.audit;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.core.tasks.DatabaseTransactionTaskWrapper;
import org.glite.security.voms.admin.core.tasks.VOMSExecutorService;
import org.glite.security.voms.admin.persistence.model.notification.Notification;

public class LogNotificationDelivery {

  private LogNotificationDelivery() {
  }

  public static ScheduledFuture<?> logDeliverySuccess(Notification n) {

    LogNotificationDeliveyOutcomeTask task = new LogNotificationDeliverySuccessTask(
      n);

    return VOMSExecutorService.instance()
      .schedule(new DatabaseTransactionTaskWrapper(task, false), 10,
        TimeUnit.MILLISECONDS);

  }

  public static ScheduledFuture<?> logDeliveryError(Notification n) {

    LogNotificationDeliveyOutcomeTask task = new LogNotificationDeliveryErrorTask(
      n);

    return VOMSExecutorService.instance()
      .schedule(new DatabaseTransactionTaskWrapper(task, false), 10,
        TimeUnit.MILLISECONDS);

  }

}
