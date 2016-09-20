/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.glite.security.voms.admin.notification.audit.LogNotificationDelivery;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.generic.NotificationDAO;
import org.glite.security.voms.admin.persistence.model.notification.Notification;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistentNotificationWorker implements Runnable {

  private static final Logger log = LoggerFactory
    .getLogger(PersistentNotificationWorker.class);

  private final NotificationDAO dao;
  private final NotificationSettings notificationSettings;

  private int numJobs = 5;

  private String serviceID;

  public PersistentNotificationWorker(NotificationDAO dao,
    NotificationSettings settings, String serviceID) {

    this.dao = dao;
    this.notificationSettings = settings;
    this.serviceID = serviceID;
  }

  @Override
  public void run() {

    Session session = null;
    Transaction tx = null;

    try {

      session = HibernateFactory.getFactory()
        .openSession();
      dao.setSession(session);

      tx = session.beginTransaction();

      List<Notification> notifications = dao.fetchJobs(numJobs, serviceID);

      if (notifications.size() > 0) {
        log.info("Fetched {} notifications.", notifications.size());

        for (Notification n : notifications) {
          handleNotification(n);
        }
      }

      tx.commit();

    } catch (Throwable t) {

      log.error(t.getMessage(), t);

      safeRollbackTransaction(tx);

    } finally {
      safeCloseSession(session);
    }

  }

  private void safeCloseSession(Session s) {

    if (s == null) {
      return;
    }

    try {
      s.close();
      s = null;
    } catch (HibernateException e) {
      log.error("Error closing session: {}", e.getMessage(), e);
    }
  }

  private void safeRollbackTransaction(Transaction tx) {

    if (tx == null) {
      return;
    }

    try {
      tx.rollback();
    } catch (HibernateException e) {
      log.error("Error rolling back transaction: {}", e.getMessage(), e);
    }
  }

  private void persistNotification(Notification n) {

    try {

      dao.makePersistent(n);

    } catch (Throwable t) {

      log.error("Error persisting notification {} to database: {}",
        new String[] { n.toString(), t.getMessage() }, t);
    }
  }

  private void handleNotification(Notification n) {

    log.debug("Handling notification {}", n);

    try {

      SimpleEmail e = makeEmail(n);
      String msgID = e.send();

      log.info("Successful delivery of notification {}. Msg id: {}", n, msgID);

      n.deliverySuccess(serviceID);
      LogNotificationDelivery.logDeliverySuccess(n);

    } catch (Throwable t) {

      n.deliveryError(t, serviceID);
      log.warn("Error deliverying notification {}: {}", n, t.getMessage());

      LogNotificationDelivery.logDeliveryError(n);

      if (log.isDebugEnabled()) {
        log.warn("Error deliverying notification {}: {}",
          new String[] { n.toString(), t.getMessage() }, t);
      }

    } finally {

      persistNotification(n);

    }

  }

  private SimpleEmail makeEmail(Notification n) throws EmailException {

    SimpleEmail e = new SimpleEmail();

    e.setHostName(notificationSettings.getSMTPHost());
    e.setSmtpPort(notificationSettings.getSMTPPort());

    e.setFrom(notificationSettings.getSender(), notificationSettings.getFrom());

    if (notificationSettings.getUsername() != null
      && notificationSettings.getPassword() != null) {
      e.setAuthentication(notificationSettings.getUsername(),
        notificationSettings.getPassword());
    }

    e.setTLS(notificationSettings.isTLS());

    for (String recipient : n.getRecipients()) {
      e.addTo(recipient);
    }

    e.setSubject(n.getSubject());
    e.setMsg(n.getMessage());

    return e;

  }

}
