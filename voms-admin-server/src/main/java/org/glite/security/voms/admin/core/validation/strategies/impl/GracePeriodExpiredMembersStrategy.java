/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

package org.glite.security.voms.admin.core.validation.strategies.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.core.validation.strategies.ExpiredMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiredMembersStrategy;
import org.glite.security.voms.admin.notification.ConditionalSendNotificationStrategy;
import org.glite.security.voms.admin.notification.NotificationService;
import org.glite.security.voms.admin.notification.NotificationUtil;
import org.glite.security.voms.admin.notification.PeriodicNotificationsTimeStorage;
import org.glite.security.voms.admin.notification.TimeIntervalNotificationStrategy;
import org.glite.security.voms.admin.notification.messages.ExpiredMembersWarning;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GracePeriodExpiredMembersStrategy implements
  HandleExpiredMembersStrategy, ExpiredMembersLookupStrategy {

  public static final Logger log = LoggerFactory
    .getLogger(GracePeriodExpiredMembersStrategy.class);

  public static final long DEFAULT_GRACE_PERIOD_IN_DAYS = 7;

  public static final String EXPIRED_MEMBERS_KEY = "expired-members";

  ConditionalSendNotificationStrategy notificationStrategy;

  long gracePeriod = DEFAULT_GRACE_PERIOD_IN_DAYS;

  public GracePeriodExpiredMembersStrategy(long gracePeriod,
    int notificationInterval) {

    this.gracePeriod = gracePeriod;

    this.notificationStrategy = new TimeIntervalNotificationStrategy(
      new PeriodicNotificationsTimeStorage(EXPIRED_MEMBERS_KEY),
      NotificationService.instance(), (long) notificationInterval,
      TimeUnit.DAYS);
  }

  public GracePeriodExpiredMembersStrategy(int notificationInterval) {

    this.notificationStrategy = new TimeIntervalNotificationStrategy(
      new PeriodicNotificationsTimeStorage(EXPIRED_MEMBERS_KEY),
      NotificationService.instance(), (long) notificationInterval,
      TimeUnit.DAYS);
  }

  public List<VOMSUser> findExpiredMembers() {

    return VOMSUserDAO.instance().findExpiredUsers();
  }

  protected void sendNotificationToAdmins(List<VOMSUser> expiredMembers) {

    if (expiredMembers.isEmpty())
      return;

    if (notificationStrategy.notificationRequired()) {
      log.info("Sending out notification about EXPIRED VO members.");
      ExpiredMembersWarning m = new ExpiredMembersWarning(expiredMembers);
      m.addRecipients(NotificationUtil.getAdministratorsEmailList());
      notificationStrategy.sendNotification(m);
    }

  }

  protected void suspendExpiredMembers(List<VOMSUser> expiredMembers) {

    if (expiredMembers.isEmpty())
      return;

    Date now = new Date();

    for (VOMSUser u : expiredMembers) {

      if (!u.isSuspended()) {

        long timeDiff = now.getTime() - u.getEndTime().getTime();

        if (TimeUnit.MILLISECONDS.toDays(timeDiff) > gracePeriod) {

          log.info("Suspending user '" + u
            + "' since its membership has expired and grace period is over.");

          ValidationManager.instance().suspendUser(u,
            SuspensionReason.MEMBERSHIP_EXPIRATION);

        } else {

          log.debug("User '{}' not suspended due to grace period.", u);

        }

        log.debug("Now: {}, User end time: {}, Grace period (in days): {}",
          new Object[] { now, u.getEndTime(), gracePeriod });

      } else {

        log.debug("User {} has expired but is currently already suspended.", u);
      }
    }

  }

  public void handleExpiredMembers(List<VOMSUser> expiredMembers) {

    log.debug("Handling expired members: {}", expiredMembers);

    suspendExpiredMembers(expiredMembers);
    sendNotificationToAdmins(expiredMembers);
  }

}
