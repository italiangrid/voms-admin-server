/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.glite.security.voms.admin.event.request;

import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.MainEventDataPoints;
import org.glite.security.voms.admin.event.SinglePayloadAuditableEvent;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;

@MainEventDataPoints({"requestorSubject", "requestorIssuer"})
public abstract class UserRequestEvent<T extends Request> extends SinglePayloadAuditableEvent<T> {

  public UserRequestEvent(EventCategory type, T payload) {

    super(type, payload);
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    Request r = getPayload();

    RequesterInfo requestor = r.getRequesterInfo();

    e.addDataPoint("requestorGivenName", requestor.getName());

    e.addDataPoint("requestorSurname", requestor.getSurname());

    e.addDataPoint("requestorSubject", requestor.getCertificateSubject());

    e.addDataPoint("requestorIssuer", requestor.getCertificateIssuer());

    e.addDataPoint("requestorIsVOMember", requestor.getVoMember());

    e.addDataPoint("requestorEmailAddress", requestor.getEmailAddress());

  }
}
