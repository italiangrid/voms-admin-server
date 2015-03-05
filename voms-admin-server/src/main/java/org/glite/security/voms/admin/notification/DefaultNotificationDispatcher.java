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
package org.glite.security.voms.admin.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventMask;
import org.glite.security.voms.admin.event.user.SignAUPTaskAssignedEvent;
import org.glite.security.voms.admin.event.user.UserMembershipExpired;
import org.glite.security.voms.admin.event.user.UserSuspendedEvent;
import org.glite.security.voms.admin.notification.messages.AdminTargetedUserSuspensionMessage;
import org.glite.security.voms.admin.notification.messages.SignAUPMessage;
import org.glite.security.voms.admin.notification.messages.UserMembershipExpiredMessage;
import org.glite.security.voms.admin.notification.messages.UserTargetedUserSuspensionMessage;

public class DefaultNotificationDispatcher extends BaseNotificationDispatcher {

  public static final Logger log = LoggerFactory
    .getLogger(DefaultNotificationDispatcher.class);

  private static DefaultNotificationDispatcher instance = null;

  private DefaultNotificationDispatcher() {

    super(null);
  }

  public static final DefaultNotificationDispatcher instance() {

    if (instance == null)
      instance = new DefaultNotificationDispatcher();

    return instance;

  }

  public void fire(Event e) {

    if (e instanceof UserSuspendedEvent) {

      handle((UserSuspendedEvent) e);

    } else if (e instanceof SignAUPTaskAssignedEvent) {

      handle((SignAUPTaskAssignedEvent) e);

    } else if (e instanceof UserMembershipExpired) {

      handle((UserMembershipExpired) e);

    }

  }

  public EventMask getMask() {

    return null;
  }

  protected void handle(UserSuspendedEvent e) {

    AdminTargetedUserSuspensionMessage msg = new AdminTargetedUserSuspensionMessage(
      e.getUser(), e.getReason().getMessage());

    UserTargetedUserSuspensionMessage usrMsg = new UserTargetedUserSuspensionMessage(
      e.getUser(), e.getReason().getMessage());

    msg.addRecipients(NotificationUtil.getAdministratorsEmailList());
    usrMsg.addRecipient(e.getUser().getEmailAddress());

    NotificationServiceFactory.getNotificationService().send(msg);
    NotificationServiceFactory.getNotificationService().send(usrMsg);

  }

  protected void handle(SignAUPTaskAssignedEvent e) {

    SignAUPMessage msg = new SignAUPMessage(e.getUser(), e.getAup());
    msg.addRecipient(e.getUser().getEmailAddress());
    NotificationServiceFactory.getNotificationService().send(msg);

  }

  protected void handle(UserMembershipExpired e) {

    UserMembershipExpiredMessage msg = new UserMembershipExpiredMessage(
      ((UserMembershipExpired) e).getUser());
    msg.addRecipients(NotificationUtil.getAdministratorsEmailList());
    NotificationServiceFactory.getNotificationService().send(msg);

  }

}
