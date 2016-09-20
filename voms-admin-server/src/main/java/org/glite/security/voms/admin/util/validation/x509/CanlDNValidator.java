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
package org.glite.security.voms.admin.util.validation.x509;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.util.validation.x509.DnValidationResult.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.NamespaceCheckingMode;
import eu.emi.security.authn.x509.StoreUpdateListener;
import eu.emi.security.authn.x509.helpers.ObserversHandler;
import eu.emi.security.authn.x509.helpers.ns.NamespacePolicy;
import eu.emi.security.authn.x509.helpers.trust.OpensslTrustAnchorStore;
import eu.emi.security.authn.x509.helpers.trust.OpensslTrustAnchorStoreImpl;
import eu.emi.security.authn.x509.impl.OpensslNameUtils;
import eu.emi.security.authn.x509.impl.X500NameUtils;

public class CanlDNValidator implements DnValidator, StoreUpdateListener {

  public static final Logger LOG = LoggerFactory
    .getLogger(CanlDNValidator.class);

  final OpensslTrustAnchorStore trustAnchorStore;
  final Timer trustStoreTimer = new Timer("CanlDNValidator timer", true);
  final NamespaceCheckingMode nsMode = NamespaceCheckingMode.EUGRIDPMA_AND_GLOBUS_REQUIRE;

  public CanlDNValidator(String trustAnchorsDir, boolean openssl1Mode) {
    trustAnchorStore = new OpensslTrustAnchorStoreImpl(trustAnchorsDir,
      trustStoreTimer, TimeUnit.HOURS.toMillis(4), nsMode.globusEnabled(),
      nsMode.euGridPmaEnabled(),
      new ObserversHandler(Collections.singletonList(this)), openssl1Mode);
  }

  @SuppressWarnings("deprecation")
  @Override
  public DnValidationResult validate(String issuerSubject,
    String certificateSubject) {

    Validate.notNull(issuerSubject, "issuerSubject must be non-null");
    Validate.notNull(certificateSubject, "certificateSubject must be non-null");

    String issuerSubjectRfc = OpensslNameUtils.opensslToRfc2253(issuerSubject);
    String certificateSubjectRfc = OpensslNameUtils
      .opensslToRfc2253(certificateSubject);

    LOG.debug("issuer rfc2253 string: {}", issuerSubjectRfc);
    LOG.debug("subject rfc2253 string: {}", certificateSubjectRfc);

    X500Principal issuerPrincipal, subjectPrincipal;

    try {
      issuerPrincipal = X500NameUtils.getX500Principal(issuerSubjectRfc);

      subjectPrincipal = X500NameUtils.getX500Principal(certificateSubjectRfc);

    } catch (IOException e) {
      LOG.error("Error converting subject to X500Principal: {}", e.getMessage(),
        e);

      throw new DnValidationError(e.getMessage(), e);
    }

    List<NamespacePolicy> policies = trustAnchorStore.getPmaNsStore()
      .getPolicies(new X500Principal[] { subjectPrincipal, issuerPrincipal },
        1);

    DnValidationResult.Builder resultBuilder = DnValidationResult.build()
      .dn(subjectPrincipal)
      .ca(issuerPrincipal);

    if (policies == null || policies.isEmpty()) {
      resultBuilder.error(ValidationError.NAMESPACE_NOT_FOUND);
      return resultBuilder.build();
    }

    for (NamespacePolicy p : policies) {
      if (p.isSubjectMatching(subjectPrincipal)) {
        LOG.debug("Found matching policy {} for subject {}. Permit: {}",
          new Object[] { p.getIdentification(), subjectPrincipal,
            new Boolean(p.isPermit()) });

        return resultBuilder.policy(p)
          .build();
      }
    }

    LOG.debug("No policy found for subject {}", subjectPrincipal);
    return resultBuilder.error(ValidationError.NO_MATCHING_POLICY)
      .build();
  }

  @Override
  public void loadingNotification(String location, String type, Severity level,
    Exception cause) {

  }

}
