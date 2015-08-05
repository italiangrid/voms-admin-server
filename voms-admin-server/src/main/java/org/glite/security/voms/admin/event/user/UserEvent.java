package org.glite.security.voms.admin.event.user;

import static org.glite.security.voms.admin.event.auditing.NullHelper.nullSafeValue;

import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.SinglePayloadAuditableEvent;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;


public abstract class UserEvent extends SinglePayloadAuditableEvent<VOMSUser> {

  protected UserEvent(EventCategory type, VOMSUser payload) {

    super(type, payload);

  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {
  
    VOMSUser user = getPayload();
  
    e.addDataPoint("userId", nullSafeValue(user.getId()));
    e.addDataPoint("userName", nullSafeValue(user.getName()));
    e.addDataPoint("userSurname", nullSafeValue(user.getSurname()));
    e.addDataPoint("userInstitution", nullSafeValue(user.getInstitution()));
    e.addDataPoint("userAddress", nullSafeValue(user.getAddress()));
    e.addDataPoint("userPhoneNumber", nullSafeValue(user.getPhoneNumber()));
    e.addDataPoint("userEmailAddress", user.getEmailAddress());
    e.addDataPoint("userIsSuspended", nullSafeValue(user.getSuspended()));
    e.addDataPoint("userSuspensionReason",
      nullSafeValue(user.getSuspensionReason()));
    e.addDataPoint("userSuspensionReasonCode",
      nullSafeValue(user.getSuspensionReasonCode()));
  
    e.addDataPoint("userMembershipExpirationDate", 
      nullSafeValue(user.getEndTime()));
    
    e.addDataPoint("userOrgDbId", 
      nullSafeValue(user.getOrgDbId()));
    
    int certificateCounter = 0;
  
    for (Certificate c : user.getCertificates()) {
      String certificateLabel = String.format("userCertificate%d",
        certificateCounter++);
  
      e.addDataPoint(certificateLabel, c.toString());
    }
  
  }

  
}
