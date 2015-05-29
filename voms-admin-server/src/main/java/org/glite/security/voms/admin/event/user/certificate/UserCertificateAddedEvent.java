package org.glite.security.voms.admin.event.user.certificate;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

@EventDescription(message = "added certificate '%s' to user '%s %s'",
  params = { "userCertificate", "userName", "userSurname" })
public class UserCertificateAddedEvent extends UserCertificateEvent {

  public UserCertificateAddedEvent(VOMSUser payload, Certificate certificate) {

    super(payload, certificate);
  }

}
