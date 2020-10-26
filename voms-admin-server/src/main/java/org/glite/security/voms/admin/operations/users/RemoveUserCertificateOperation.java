/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glite.security.voms.admin.operations.users;

import java.security.cert.X509Certificate;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.certificate.UserCertificateRemoved;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.CertificateDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchCertificateException;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class RemoveUserCertificateOperation extends BaseVomsOperation {

  X509Certificate theCert;

  Certificate cert;

  String subject;
  String issuer;

  private RemoveUserCertificateOperation(Certificate c) {
    this.cert = c;
  }

  private RemoveUserCertificateOperation(X509Certificate c) {

    theCert = c;

  }

  private RemoveUserCertificateOperation(String subject, String issuer) {

    this.subject = subject;
    this.issuer = issuer;
  }

  public static RemoveUserCertificateOperation instance(X509Certificate cert) {

    return new RemoveUserCertificateOperation(cert);

  }

  public static RemoveUserCertificateOperation instance(String subject,
    String issuer) {

    return new RemoveUserCertificateOperation(subject, issuer);
  }
  
  public static RemoveUserCertificateOperation instance(Certificate cert){
    return new RemoveUserCertificateOperation(cert);
  }

  private Certificate lookupCert() {

    Certificate cert = null;

    if (theCert != null) {

      cert = CertificateDAO.instance()
        .find(theCert);

    } else {

      cert = CertificateDAO.instance()
        .lookup(subject, issuer);

    }

    if (cert == null) {
      throw new NoSuchCertificateException(
        "No certificate found matching subject '" + subject + ", " + issuer
          + "'.");
    }
    
    return cert;

  }

  @Override
  protected Object doExecute() {

    if (cert == null){
      cert = lookupCert();
    }
    
    VOMSUser u = cert.getUser();

    VOMSUserDAO.instance().deleteCertificate(u, cert);

    EventManager.instance()
      .dispatch(new UserCertificateRemoved(u, cert));

    return cert;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(),
      VOMSPermission.getContainerRWPermissions()
        .setMembershipRWPermission());

  }

}
