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
package org.glite.security.voms.admin.view.actions.user;

import org.glite.security.voms.admin.persistence.dao.CertificateDAO;
import org.glite.security.voms.admin.persistence.model.Certificate;

public abstract class CertificateActionSupport extends UserActionSupport {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  Long certificateId;

  Certificate certificate;

  /**
   * @return the certificateId
   */
  public Long getCertificateId() {

    return certificateId;
  }

  /**
   * @param certificateId
   *          the certificateId to set
   */
  public void setCertificateId(Long certificateId) {

    this.certificateId = certificateId;
  }

  /**
   * @return the certificate
   */
  public Certificate getCertificate() {

    return certificate;
  }

  /**
   * @param certificate
   *          the certificate to set
   */
  public void setCertificate(Certificate certificate) {

    this.certificate = certificate;
  }

  @Override
  public void prepare() throws Exception {

    if (certificate == null)
      certificate = CertificateDAO.instance().findById(certificateId);

    super.prepare();

  }

}
