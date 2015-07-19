package org.glite.security.voms.admin.event.user.certificate;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

@EventDescription(message = "restored certificate '%s' for user '%s %s'",
  params = { "userCertificate", "userName", "userSurname" })
public class UserCertificateRestored extends UserCertificateEvent {

  public UserCertificateRestored(VOMSUser payload, Certificate certificate) {

    super(payload, certificate);

  }

}
