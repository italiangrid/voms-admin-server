package org.glite.security.voms.admin.event.user.certificate;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

@EventDescription(message = "suspended certificate '%s' for user '%s %s'",
  params = { "userCertificate", "userName", "userSurname" })
public class UserCertificateSuspended extends UserCertificateEvent {

  public UserCertificateSuspended(VOMSUser payload, Certificate certificate) {

    super(payload, certificate);

  }

}
