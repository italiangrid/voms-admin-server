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

import java.util.List;

import org.glite.security.voms.admin.core.validation.strategies.HandleExpiringMembersStrategy;
import org.glite.security.voms.admin.notification.ConditionalSendNotificationStrategy;
import org.glite.security.voms.admin.notification.NotificationService;
import org.glite.security.voms.admin.notification.NotificationUtil;
import org.glite.security.voms.admin.notification.PeriodicNotificationsTimeStorage;
import org.glite.security.voms.admin.notification.TimeIntervalNotificationStrategy;
import org.glite.security.voms.admin.notification.messages.MembershipExpirationWarning;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendWarningAboutExpiringMembersStrategy implements
  HandleExpiringMembersStrategy {

  public static final Logger log = LoggerFactory
    .getLogger(SendWarningAboutExpiringMembersStrategy.class);

  public static final String NOTIFICATION_KEY = "expiring-members";

  ConditionalSendNotificationStrategy notificationStrategy;

  public SendWarningAboutExpiringMembersStrategy() {

    notificationStrategy = new TimeIntervalNotificationStrategy(
      new PeriodicNotificationsTimeStorage(NOTIFICATION_KEY),
      NotificationService.instance());
  }

  public void handleMembersAboutToExpire(List<VOMSUser> expiringMembers) {

    if (log.isDebugEnabled())
      log.debug("Handling members about to expire: {}", expiringMembers);

    if (expiringMembers == null | expiringMembers.isEmpty())
      return;

    if (notificationStrategy.notificationRequired()) {

      log.debug("Sending out notification about expiring VO members.");
      MembershipExpirationWarning m = new MembershipExpirationWarning(
        expiringMembers);
      m.addRecipients(NotificationUtil.getAdministratorsEmailList());
      notificationStrategy.sendNotification(m);
    }
  }

}
