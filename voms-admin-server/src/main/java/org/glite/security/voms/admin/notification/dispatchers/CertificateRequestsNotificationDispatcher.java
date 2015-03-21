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

import java.util.EnumSet;

import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.registration.CertificateRequestApprovedEvent;
import org.glite.security.voms.admin.event.registration.CertificateRequestRejectedEvent;
import org.glite.security.voms.admin.event.registration.CertificateRequestSubmittedEvent;
import org.glite.security.voms.admin.notification.BaseNotificationDispatcher;
import org.glite.security.voms.admin.notification.NotificationService;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;

public class CertificateRequestsNotificationDispatcher extends
  BaseNotificationDispatcher {

  private static volatile CertificateRequestsNotificationDispatcher INSTANCE;

  public static CertificateRequestsNotificationDispatcher instance() {

    if (INSTANCE == null)
      INSTANCE = new CertificateRequestsNotificationDispatcher();

    return INSTANCE;
  }

  private CertificateRequestsNotificationDispatcher() {

    super(EnumSet.of(EventCategory.CertificateRequestEvent));
  }

  public void fire(Event e) {

    if (e instanceof CertificateRequestSubmittedEvent) {

      CertificateRequestSubmittedEvent ee = (CertificateRequestSubmittedEvent) e;

      HandleRequest msg = new HandleRequest(ee.getRequest(),
        ee.getManagementURL());

      NotificationService.instance().send(msg);
    }

    if (e instanceof CertificateRequestApprovedEvent) {

      RequestApproved msg = new RequestApproved(
        ((CertificateRequestApprovedEvent) e).getRequest());
      NotificationService.instance().send(msg);
    }

    if (e instanceof CertificateRequestRejectedEvent) {
      RequestRejected msg = new RequestRejected(
        ((CertificateRequestRejectedEvent) e).getRequest(), null);
      NotificationService.instance().send(msg);
    }

  }

}
