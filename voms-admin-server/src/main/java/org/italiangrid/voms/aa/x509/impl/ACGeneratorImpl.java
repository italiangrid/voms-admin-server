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
package org.italiangrid.voms.aa.x509.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.bouncycastle.cert.X509AttributeCertificateHolder;
import org.glite.security.voms.admin.error.IllegalStateException;
import org.italiangrid.voms.aa.RequestContext;
import org.italiangrid.voms.aa.x509.ACGenerator;
import org.italiangrid.voms.asn1.VOMSACGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.impl.PEMCredential;

public enum ACGeneratorImpl implements ACGenerator {

  INSTANCE;

  public static final Logger logger = LoggerFactory
    .getLogger(ACGeneratorImpl.class);

  private volatile boolean configured = false;

  private VOMSACGenerator acGenerator;

  public synchronized void configure(PEMCredential aaCredential) {

    if (!configured) {
      acGenerator = new VOMSACGenerator(aaCredential);
      configured = true;
    }

  }

  private BigInteger computeSerialNumber() {

    ByteBuffer buf = ByteBuffer.allocate(16);

    UUID r = UUID.randomUUID();

    buf.putLong(r.getMostSignificantBits());
    buf.putLong(r.getLeastSignificantBits());

    buf.flip();

    BigInteger bi = new BigInteger(buf.array());
    return bi.abs();

  }

  @Override
  public byte[] generateVOMSAC(RequestContext context) throws IOException {

    if (!configured)
      throw new IllegalStateException("AC generator is not configured!");

    BigInteger serialNo = computeSerialNumber();

    X509AttributeCertificateHolder ac = acGenerator
      .generateVOMSAttributeCertificate(context.getResponse().getIssuedFQANs(),
        context.getResponse().getIssuedGAs(), context.getResponse()
          .getTargets(), context.getRequest().getHolderCert(), serialNo,
        context.getResponse().getNotBefore(), context.getResponse()
          .getNotAfter(), context.getVOName(), context.getHost(), context
          .getPort());

    return ac.getEncoded();
  }
}
