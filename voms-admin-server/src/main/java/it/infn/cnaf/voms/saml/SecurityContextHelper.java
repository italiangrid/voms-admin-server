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

/**************************************************************************

 Copyright 2006-2007 Istituto Nazionale di Fisica Nucleare (INFN)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 File : SecurityContextImpl.java

 Authors: Valerio Venturi <valerio.venturi@cnaf.infn.it>

 **************************************************************************/

package it.infn.cnaf.voms.saml;

import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.util.DNUtil;

/**
 * @author Valerio Venturi <valerio.venturi@cnaf.infn.it>
 *
 */
public class SecurityContextHelper {

  static private Logger logger = LoggerFactory
    .getLogger(SecurityContextHelper.class);

  private X509Certificate[] certificateChain;

  /**
   * 
   */
  public SecurityContextHelper(HttpServletRequest httpServletRequest) {

    certificateChain = (X509Certificate[]) httpServletRequest
      .getAttribute("javax.servlet.request.X509Certificate");

    if (certificateChain == null)
      throw new Error("Not authenticated.");

    logger
      .info("Authenticated subject "
        + DNUtil.getOpenSSLSubject(certificateChain[0]
          .getSubjectX500Principal()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * it.infn.cnaf.voms.aa.presentation.security.SecurityContext#getCertificate()
   */
  public X509Certificate getCertificate() {

    return certificateChain[0];
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * it.infn.cnaf.voms.aa.presentation.security.SecurityContext#getCertificateChain
   * ()
   */
  public X509Certificate[] getCertificateChain() {

    return certificateChain;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * it.infn.cnaf.voms.aa.presentation.security.SecurityContext#getPrincipal()
   */
  public X500Principal getX500Principal() {

    return certificateChain[0].getSubjectX500Principal();
  }

  public boolean is(X500Principal issuer) {

    return certificateChain[0].getSubjectX500Principal().equals(issuer);
  }

}
