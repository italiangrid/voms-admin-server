package org.glite.security.voms.admin.event.user.certificate;

import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.user.UserEvent;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class UserCertificateEvent extends UserEvent {

  final Certificate certificate;

  public UserCertificateEvent(VOMSUser payload, Certificate certificate) {

    super(EventCategory.UserCertificateEvent, payload);
    this.certificate = certificate;

  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    super.decorateAuditEvent(e);
    e.addDataPoint("userCertificate", certificate.toString());

  }

}
