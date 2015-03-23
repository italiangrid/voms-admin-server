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

package org.glite.security.voms.admin.notification.dispatchers;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.request.VOMembershipRequestApprovedEvent;
import org.glite.security.voms.admin.event.request.VOMembershipRequestConfirmedEvent;
import org.glite.security.voms.admin.event.request.VOMembershipRequestRejectedEvent;
import org.glite.security.voms.admin.event.request.VOMembershipRequestSubmittedEvent;
import org.glite.security.voms.admin.notification.BaseNotificationDispatcher;
import org.glite.security.voms.admin.notification.NotificationService;
import org.glite.security.voms.admin.notification.NotificationUtil;
import org.glite.security.voms.admin.notification.messages.ConfirmRequest;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class VOMembershipNotificationDispatcher extends
  BaseNotificationDispatcher {

  private static VOMembershipNotificationDispatcher instance;

  public static VOMembershipNotificationDispatcher instance() {

    if (instance == null)
      instance = new VOMembershipNotificationDispatcher();

    return instance;
  }

  private VOMembershipNotificationDispatcher() {

    super(EnumSet.of(EventCategory.VOMembershipRequestEvent));

  }

  public List<String> resolveEmailAddresses(VOMembershipRequestConfirmedEvent e) {

    if (e.getPayload().getRequesterInfo().getManagerEmail() == null) {
      return NotificationUtil.getAdministratorsEmailList(
        VOMSContext.getVoContext(), VOMSPermission.getRequestsRWPermissions());
    }

    return Arrays.asList(e.getPayload().getRequesterInfo().getManagerEmail());

  }

  public void fire(Event e) {

    if (e instanceof VOMembershipRequestSubmittedEvent) {
      VOMembershipRequestSubmittedEvent ee = (VOMembershipRequestSubmittedEvent) e;

      String recipient = ee.getPayload().getRequesterInfo().getEmailAddress();

      ConfirmRequest msg = new ConfirmRequest(recipient, ee.getConfirmURL(),
        ee.getCancelURL());
      NotificationService.instance().send(msg);

    } else if (e instanceof VOMembershipRequestConfirmedEvent) {

      VOMembershipRequestConfirmedEvent ee = (VOMembershipRequestConfirmedEvent) e;

      List<String> admins = resolveEmailAddresses(ee);

      HandleRequest msg = new HandleRequest(ee.getPayload(), ee.getUrl(),
        admins);

      NotificationService.instance().send(msg);

    } else if (e instanceof VOMembershipRequestApprovedEvent) {

      RequestApproved msg = new RequestApproved(
        ((VOMembershipRequestApprovedEvent) e).getPayload());

      NotificationService.instance().send(msg);

    } else if (e instanceof VOMembershipRequestRejectedEvent) {

      RequestRejected msg = new RequestRejected(
        ((VOMembershipRequestRejectedEvent) e).getPayload(), null);
      NotificationService.instance().send(msg);

    }

  }

}
