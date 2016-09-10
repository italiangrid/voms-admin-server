package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.certificate.UserRemovedOwnCertificateEvent;
import org.glite.security.voms.admin.operations.BaseUserAdministrativeOperation;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class RemoveOwnCertificateOperation
  extends BaseUserAdministrativeOperation {

  final Certificate cert;

  public RemoveOwnCertificateOperation(Certificate cert) {

    this.cert = cert;
    setAuthorizedUser(cert.getUser());
  }

  @Override
  protected void setupPermissions() {

    // Do nothing
  }

  @Override
  protected Object doExecute() {

    CurrentAdmin currentAdmin = CurrentAdmin.instance();
    VOMSUser user = currentAdmin.getVoUser();

    if (user == null) {
      throw new VOMSException(
        "No VO user membership linked with the current authenticated user");
    }

    if (currentAdmin.is(cert)) {

      throw new VOMSException(
        "A user can only remove a certificate that is not in use.");
    }

    user.getCertificates()
      .remove(cert);

    EventManager.instance()
      .dispatch(new UserRemovedOwnCertificateEvent(user, cert));

    return cert;
  }

}
