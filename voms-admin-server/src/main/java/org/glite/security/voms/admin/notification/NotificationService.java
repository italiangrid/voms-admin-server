/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.notification;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.core.tasks.DatabaseTransactionTaskWrapper;
import org.glite.security.voms.admin.core.tasks.VOMSExecutorService;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.vo.notification.NotificationDeliveredEvent;
import org.glite.security.voms.admin.event.vo.notification.NotificationDeliveryErrorEvent;
import org.glite.security.voms.admin.notification.messages.VOMSNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationService implements NotificationServiceIF {

  private static final Logger log = LoggerFactory
    .getLogger(NotificationService.class);

  private static NotificationServiceIF singleton = null;

  private LinkedBlockingQueue<VOMSNotification> outgoingMessages = new LinkedBlockingQueue<VOMSNotification>();

  private ExecutorService executorService = Executors.newSingleThreadExecutor();

  int maxDeliveryAttemptCount = VOMSConfiguration.instance()
    .getInt("voms.noification.max_delivery_attempt_count", 5);

  long sleepTimeBeforeRetry = 30;

  private NotificationService() {

    executorService.submit(new NotificationRunner(outgoingMessages));

  }

  public synchronized static void shutdown() {

    if (singleton == null) {
      log.debug(
        "Notification service has not been started and, as such, cannot be shut down.");
      return;
    }

    singleton.shutdownNow();

  }

  public synchronized static NotificationServiceIF instance() {

    if (singleton == null)
      singleton = new NotificationService();

    return singleton;
  }

  public void send(VOMSNotification n) {

    if (!VOMSConfiguration.instance()
      .getBoolean(VOMSConfigurationConstants.NOTIFICATION_DISABLED, false)) {
      log.debug("Adding notification '{}' to outgoing message queue.", n);
      outgoingMessages.add(n);
    } else {
      log.warn(
        "Outgoing notification {} will be discarded as the notification service is DISABLED.",
        n.toString());
    }

  }

  class NotificationRunner implements Runnable {

    final LinkedBlockingQueue<VOMSNotification> outgoingQueue;

    public NotificationRunner(
      LinkedBlockingQueue<VOMSNotification> outgoingQueue) {

      this.outgoingQueue = outgoingQueue;
    }

    private void logSuccessfulDelivery(VOMSNotification n) {

      LogNotificationOutcomeTask task = LogNotificationOutcomeTask
        .notificationSuccess(n);

      VOMSExecutorService.instance()
        .schedule(new DatabaseTransactionTaskWrapper(task, false), 10,
          TimeUnit.MILLISECONDS);
    }

    private void logDeliveryError(VOMSNotification n) {

      LogNotificationOutcomeTask task = LogNotificationOutcomeTask
        .notificationError(n);

      VOMSExecutorService.instance()
        .schedule(new DatabaseTransactionTaskWrapper(task, false), 10,
          TimeUnit.MILLISECONDS);
    }

    public void run() {

      for (;;) {

        boolean deliveryHadErrors = false;

        try {

          VOMSNotification n = outgoingQueue.take();
          log.debug("Fetched outgoing message " + n);

          try {
            n.send();
            deliveryHadErrors = false;
            log.info("Notification '{}' delivered succesfully.", n);
            logSuccessfulDelivery(n);

          } catch (VOMSNotificationException e) {

            deliveryHadErrors = true;
            log.error("Error dispatching email notification '{}'", n, e);

            if (n.getDeliveryAttemptCount() < maxDeliveryAttemptCount) {
              outgoingQueue.put(n);
            } else {

              log.warn(
                "Discarding notification '{}' after {} failed delivery attempts.",
                n, n.getDeliveryAttemptCount());

              logDeliveryError(n);

            }

          } catch (Throwable t) {

            log.error("Error dispatching email notification '" + n + "': "
              + t.getClass()
                .getName()
              + " - " + t.getMessage());
            if (log.isDebugEnabled())
              log.error("Error dispatching email notification '" + n + "': "
                + t.getMessage(), t);

            deliveryHadErrors = true;

          }

          if (deliveryHadErrors && !outgoingQueue.isEmpty())
            TimeUnit.SECONDS.sleep(sleepTimeBeforeRetry);

        } catch (InterruptedException e) {

          return;
        }
      }

    }
  }

  static class LogNotificationOutcomeTask implements Runnable {

    private final VOMSNotification notification;
    private final boolean deliveryError;

    public static LogNotificationOutcomeTask notificationSuccess(
      VOMSNotification n) {

      return new LogNotificationOutcomeTask(n, false);
    }

    public static LogNotificationOutcomeTask notificationError(
      VOMSNotification n) {

      return new LogNotificationOutcomeTask(n, true);
    }

    private LogNotificationOutcomeTask(VOMSNotification n, boolean error) {
      this.notification = n;
      deliveryError = error;
    }

    @Override
    public void run() {

      if (deliveryError) {
        EventManager.instance()
          .dispatch(new NotificationDeliveryErrorEvent(notification));
      } else {
        EventManager.instance()
          .dispatch(new NotificationDeliveredEvent(notification));
      }
    }

  }

  /**
   * @return @see java.util.concurrent.ExecutorService#shutdownNow()
   */
  public List<Runnable> shutdownNow() {

    return executorService.shutdownNow();
  }

  @Override
  public void start() {

    // TODO Auto-generated method stub

  }

}
