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
package org.glite.security.voms.admin.notification.dispatchers;

import java.util.EnumSet;

import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.request.MembershipRemovalApprovedEvent;
import org.glite.security.voms.admin.event.request.MembershipRemovalRejectedEvent;
import org.glite.security.voms.admin.event.request.MembershipRemovalRequestEvent;
import org.glite.security.voms.admin.event.request.MembershipRemovalSubmittedEvent;
import org.glite.security.voms.admin.notification.BaseNotificationDispatcher;
import org.glite.security.voms.admin.notification.NotificationServiceFactory;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;
import org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest;

public class MembershipRemovalNotificationDispatcher
  extends BaseNotificationDispatcher {

  private static transient MembershipRemovalNotificationDispatcher INSTANCE;

  public static MembershipRemovalNotificationDispatcher instance() {

    if (INSTANCE == null) {
      INSTANCE = new MembershipRemovalNotificationDispatcher();
    }

    return INSTANCE;
  }

  private MembershipRemovalNotificationDispatcher() {

    super(EnumSet.of(EventCategory.MembershipRemovalRequestEvent));

  }

  public void fire(Event e) {

    MembershipRemovalRequestEvent event = (MembershipRemovalRequestEvent) e;
    MembershipRemovalRequest req = event.getPayload();

    if (event instanceof MembershipRemovalSubmittedEvent) {

      MembershipRemovalSubmittedEvent ee = (MembershipRemovalSubmittedEvent) event;
      HandleRequest msg = new HandleRequest(req, ee.getManagementURL());
      NotificationServiceFactory.getNotificationService()
        .send(msg);

    } else if (event instanceof MembershipRemovalApprovedEvent) {

      RequestApproved msg = new RequestApproved(
        ((MembershipRemovalApprovedEvent) event).getPayload());
      NotificationServiceFactory.getNotificationService()
        .send(msg);

    } else if (event instanceof MembershipRemovalRejectedEvent) {

      RequestRejected msg = new RequestRejected(
        ((MembershipRemovalRejectedEvent) event).getPayload(), null);

      NotificationServiceFactory.getNotificationService()
        .send(msg);

    }

  }
}
