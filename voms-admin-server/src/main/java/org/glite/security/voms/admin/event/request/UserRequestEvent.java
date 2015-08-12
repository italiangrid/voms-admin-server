package org.glite.security.voms.admin.event.request;

import static org.glite.security.voms.admin.event.auditing.NullHelper.nullSafeValue;

import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.SinglePayloadAuditableEvent;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;

public class UserRequestEvent<T extends Request> extends
  SinglePayloadAuditableEvent<T> {

  public UserRequestEvent(EventCategory type, T payload) {

    super(type, payload);
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    Request r = getPayload();

    RequesterInfo requestor = r.getRequesterInfo();
  
    e.addDataPoint("requestorGivenName", 
      nullSafeValue(requestor.getName()));
    
    e.addDataPoint("requestorSurname", 
      nullSafeValue(requestor.getSurname()));
    
    e.addDataPoint("requestorSubject", requestor
      .getCertificateSubject());
    
    e.addDataPoint("requestorIssuer", requestor
      .getCertificateIssuer());
    
    e.addDataPoint("requestorIsVOMember", 
      nullSafeValue(requestor.getVoMember()));
    
    e.addDataPoint("requestorEmailAddress",
      requestor.getEmailAddress());
    
  }
}
