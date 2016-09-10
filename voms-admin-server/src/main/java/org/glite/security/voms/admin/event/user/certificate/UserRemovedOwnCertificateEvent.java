package org.glite.security.voms.admin.event.user.certificate;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

@EventDescription(
  message = "removed secondary certificate '%s' from his/her membership",
  params = { "userCertificate" })
public class UserRemovedOwnCertificateEvent extends UserCertificateEvent {

  public UserRemovedOwnCertificateEvent(VOMSUser payload,
    Certificate certificate) {
    super(payload, certificate);
  }

}
