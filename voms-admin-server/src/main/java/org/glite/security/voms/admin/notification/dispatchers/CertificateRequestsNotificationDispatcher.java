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

import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.request.CertificateRequestApprovedEvent;
import org.glite.security.voms.admin.event.request.CertificateRequestEvent;
import org.glite.security.voms.admin.event.request.CertificateRequestRejectedEvent;
import org.glite.security.voms.admin.event.request.CertificateRequestSubmittedEvent;
import org.glite.security.voms.admin.notification.BaseNotificationDispatcher;
import org.glite.security.voms.admin.notification.NotificationService;
import org.glite.security.voms.admin.notification.messages.HandleRequest;
import org.glite.security.voms.admin.notification.messages.RequestApproved;
import org.glite.security.voms.admin.notification.messages.RequestRejected;

public class CertificateRequestsNotificationDispatcher extends
  BaseNotificationDispatcher<CertificateRequestEvent> {

  private static volatile CertificateRequestsNotificationDispatcher INSTANCE;

  public static CertificateRequestsNotificationDispatcher instance() {

    if (INSTANCE == null)
      INSTANCE = new CertificateRequestsNotificationDispatcher();

    return INSTANCE;
  }

  private CertificateRequestsNotificationDispatcher() {

    super(EnumSet.of(EventCategory.CertificateRequestEvent));
  }

  public void fire(CertificateRequestEvent e) {

    if (e instanceof CertificateRequestSubmittedEvent) {

      CertificateRequestSubmittedEvent ee = (CertificateRequestSubmittedEvent) e;

      HandleRequest msg = new HandleRequest(ee.getPayload(),
        ee.getManagementURL());

      NotificationService.instance().send(msg);
    }

    if (e instanceof CertificateRequestApprovedEvent) {

      RequestApproved msg = new RequestApproved(
        ((CertificateRequestApprovedEvent) e).getPayload());
      
      NotificationService.instance().send(msg);
    }

    if (e instanceof CertificateRequestRejectedEvent) {
      RequestRejected msg = new RequestRejected(
        ((CertificateRequestRejectedEvent) e).getPayload(), null);
      NotificationService.instance().send(msg);
    }

  }

}
