/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.certificate.UserCertificateRestored;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.CertificateDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchCertificateException;
import org.glite.security.voms.admin.persistence.model.Certificate;

public class RestoreUserCertificateOperation extends BaseVomsOperation {

  Certificate certificate;

  private RestoreUserCertificateOperation(Certificate c) {

    certificate = c;
  }

  private RestoreUserCertificateOperation(String subject, String issuerSubject) {

    certificate = CertificateDAO.instance().findByDNCA(subject, issuerSubject);
    if (certificate == null)
      throw new NoSuchCertificateException("Certificate identified by '"
        + subject + "', '" + issuerSubject + "' not found!");

  }

  public static RestoreUserCertificateOperation instance(String subject,
    String issuerSubject) {

    return new RestoreUserCertificateOperation(subject, issuerSubject);

  }

  public static RestoreUserCertificateOperation instance(Certificate c) {

    return new RestoreUserCertificateOperation(c);
  }

  @Override
  protected Object doExecute() {

    if (certificate == null)
      throw new NullArgumentException("certificate cannot be null");

    certificate.restore();
    
    EventManager.dispatch(new UserCertificateRestored(certificate.getUser(), 
      certificate));
    
    return certificate;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerReadPermission().setMembershipReadPermission()
      .setSuspendPermission());

  }

}
