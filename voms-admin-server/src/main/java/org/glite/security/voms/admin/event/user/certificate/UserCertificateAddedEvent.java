package org.glite.security.voms.admin.event.user.certificate;

import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;


public class UserCertificateAddedEvent extends UserCertificateEvent {

  public UserCertificateAddedEvent(VOMSUser payload, Certificate certificate) {

    super(payload, certificate);
    // TODO Auto-generated constructor stub
  }

}
