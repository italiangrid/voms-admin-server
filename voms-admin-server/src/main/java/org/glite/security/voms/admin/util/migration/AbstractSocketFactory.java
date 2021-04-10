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
package org.glite.security.voms.admin.util.migration;

import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import eu.emi.security.authn.x509.X509CertChainValidatorExt;
import eu.emi.security.authn.x509.impl.PEMCredential;
import eu.emi.security.authn.x509.impl.SocketFactoryCreator;

public class AbstractSocketFactory {

  protected X509CertChainValidatorExt validator;
  protected String proxyFile;
  

  protected SSLSocketFactory createSocketFactory() throws Exception {
  
    SSLContext context = SSLContext.getInstance("TLSv1");
  
    KeyManager[] keyManagers = getKeymanagers();
    TrustManager[] trustManagers = getTrustmanagers();
    SecureRandom random = getSecureRandom();
  
    context.init(keyManagers, trustManagers, random);
  
    return context.getSocketFactory();
  
  }

  protected KeyManager[] getKeymanagers() throws Exception {
  
    PEMCredential cred = new PEMCredential(new FileInputStream(proxyFile), (char[]) null);
    return new KeyManager[] {cred.getKeyManager()};
  }

  protected TrustManager[] getTrustmanagers() throws Exception {
  
    X509TrustManager trustManager = SocketFactoryCreator.getSSLTrustManager(validator);
  
    TrustManager[] trustManagers = new TrustManager[] {trustManager};
  
    return trustManagers;
  
  }

  protected SecureRandom getSecureRandom() throws NoSuchAlgorithmException {
    return new SecureRandom();
  }

}
