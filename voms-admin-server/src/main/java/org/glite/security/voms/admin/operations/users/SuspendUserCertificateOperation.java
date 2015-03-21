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
import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.certificate.UserCertificateSuspended;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.CertificateDAO;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;

public class SuspendUserCertificateOperation extends BaseVomsOperation {

  VOMSUser user;
  Certificate certificate;
  SuspensionReason reason;

  private SuspendUserCertificateOperation(VOMSUser u, Certificate c,
    SuspensionReason r) {

    user = u;
    certificate = c;
    reason = r;

  }

  public static SuspendUserCertificateOperation instance(VOMSUser u,
    Certificate c, SuspensionReason r) {

    return new SuspendUserCertificateOperation(u, c, r);
  }

  public static SuspendUserCertificateOperation instance(String dn, String ca,
    String suspensionReason) {

    Certificate c = CertificateDAO.instance().findByDNCA(dn, ca);
    SuspensionReason reason = SuspensionReason.OTHER;
    reason.setMessage(suspensionReason);
    return new SuspendUserCertificateOperation(c.getUser(), c, reason);
  }

  @Override
  protected Object doExecute() {

    if (user == null)
      throw new NullArgumentException("user cannot be null");

    if (certificate == null)
      throw new NullArgumentException("certificate cannot be null");

    if (reason == null)
      throw new NullArgumentException("reason cannot be null");

    if (!user.hasCertificate(certificate))
      throw new VOMSException("Certificate '" + certificate
        + "' is not bound to user '" + user + "'.");

    certificate.suspend(reason);
    
    EventManager.dispatch(new UserCertificateSuspended(user, certificate));
    
    return certificate;
  }

  @Override
  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getContainerReadPermission().setMembershipReadPermission()
      .setSuspendPermission());
  }

}
