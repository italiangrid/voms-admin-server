package org.glite.security.voms.admin.notification;

import java.util.List;

import org.glite.security.voms.admin.notification.messages.VOMSNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DisabledNotificationService implements NotificationServiceIF {

  INSTANCE;

  public static final Logger LOG = LoggerFactory
    .getLogger(DisabledNotificationService.class);

  @Override
  public void start() {

    LOG.info(
      "Starting DISABLED notification service: email notification going to /dev/null");
  }

  @Override
  public void send(VOMSNotification n) {

    n.buildMessage();

    LOG.warn(
      "Outgoing notification will be discarded as the notification service is DISABLED. Subject:{}, To:{}, Body:{}",
      new Object[] { n.getSubject(), n.getRecipientList(), n.getMessage() });
  }

  @Override
  public List<Runnable> shutdownNow() {

    LOG.info("Shutting down DISABLED notification service");
    return null;
  }

}
