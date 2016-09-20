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
package org.glite.security.voms.admin.servlets;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

import javax.servlet.ServletRequest;

import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.error.VOMSException;
import org.italiangrid.utils.voms.CurrentSecurityContext;
import org.italiangrid.utils.voms.SecurityContext;
import org.italiangrid.utils.voms.SecurityContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitSecurityContext {

  protected static Logger log = LoggerFactory
    .getLogger(InitSecurityContext.class);

  public static void setInternalAdminContext() {

    log.debug("Creating a new internal security context");
    SecurityContext sc = SecurityContextFactory.newVOMSSecurityContext(null);

    sc.setClientName(VOMSServiceConstants.INTERNAL_ADMIN);
    sc.setIssuerName(VOMSServiceConstants.VIRTUAL_CA);

    CurrentSecurityContext.set(sc);

  }

  public static void setContextFromRequest(final ServletRequest req) {

    log.debug("Creating a new security context");
    SecurityContext sc = SecurityContextFactory
      .newSecurityContext();

    String remote = req.getRemoteAddr();
    sc.setRemoteAddr(remote);

    X509Certificate[] cert = null;
    try {
      cert = (X509Certificate[]) req
        .getAttribute("javax.servlet.request.X509Certificate");
    } catch (Throwable t) {
      throw new VOMSException(t.getMessage(), t);
    }

    if (cert == null) {
      sc.setClientName(VOMSServiceConstants.UNAUTHENTICATED_CLIENT);
      sc.setIssuerName(VOMSServiceConstants.VIRTUAL_CA);
    } else {
      // Client certificate found.
      sc.setClientCertChain(cert);

      // Do not allow internal credentials coming from an external source.
      if (sc.getClientName() != null
        && sc.getClientName().startsWith(
          VOMSServiceConstants.INTERNAL_DN_PREFIX)) {
        log
          .error("Client name starts with internal prefix, discarding credentials: "
            + sc.getClientName());
        sc.setClientName(VOMSServiceConstants.UNAUTHENTICATED_CLIENT);
        sc.setIssuerName(VOMSServiceConstants.VIRTUAL_CA);
      } else if (sc.getIssuerName() != null
        && sc.getIssuerName().startsWith(
          VOMSServiceConstants.INTERNAL_DN_PREFIX)) {
        log
          .error("Client issuer starts with internal prefix, discarding credentials: "
            + sc.getClientName());
        sc.setClientName(VOMSServiceConstants.UNAUTHENTICATED_CLIENT);
        sc.setIssuerName(VOMSServiceConstants.VIRTUAL_CA);
      }
    }

    CurrentSecurityContext.set(sc);
  }

  public static void logConnection() {

    SecurityContext sc = (SecurityContext) CurrentSecurityContext.get();

    if (sc.getClientCert() == null) {

      log.info("Unauthenticated connection from \"{}\"", sc.getRemoteAddr());

    } else {

      String clientName = sc.getClientName();
      String issuerName = sc.getIssuerName();

      BigInteger sn = sc.getClientCert().getSerialNumber();

      String serialNumber = (sn == null) ? "NULL" : sn.toString();

      log.info("Connection from \"" + sc.getRemoteAddr() + "\" by "
        + clientName + " (issued by \"" + issuerName + "\", " + "serial "
        + serialNumber + ")");
    }
  }

}
