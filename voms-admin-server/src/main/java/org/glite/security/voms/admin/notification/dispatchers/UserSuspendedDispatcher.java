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
package org.glite.security.voms.admin.notification.dispatchers;

import java.util.EnumSet;

import org.glite.security.voms.admin.event.AbstractEventLister;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.user.UserSuspendedEvent;
import org.glite.security.voms.admin.notification.NotificationServiceFactory;
import org.glite.security.voms.admin.notification.NotificationUtil;
import org.glite.security.voms.admin.notification.messages.AdminTargetedUserSuspensionMessage;
import org.glite.security.voms.admin.notification.messages.UserTargetedUserSuspensionMessage;

public class UserSuspendedDispatcher
  extends AbstractEventLister<UserSuspendedEvent> {

  public UserSuspendedDispatcher() {

    super(EnumSet.of(EventCategory.UserLifecycleEvent),
      UserSuspendedEvent.class);
  }

  @Override
  protected void doFire(UserSuspendedEvent e) {

    AdminTargetedUserSuspensionMessage msg = new AdminTargetedUserSuspensionMessage(
      e.getPayload(), e.getReason()
        .getMessage());

    UserTargetedUserSuspensionMessage usrMsg = new UserTargetedUserSuspensionMessage(
      e.getPayload(), e.getReason()
        .getMessage());

    msg.addRecipients(NotificationUtil.getAdministratorsEmailList());
    usrMsg.addRecipient(e.getPayload()
      .getEmailAddress());

    NotificationServiceFactory.getNotificationService()
      .send(msg);
    NotificationServiceFactory.getNotificationService()
      .send(usrMsg);
  }

  public static UserSuspendedDispatcher instance() {

    return new UserSuspendedDispatcher();
  }
}
