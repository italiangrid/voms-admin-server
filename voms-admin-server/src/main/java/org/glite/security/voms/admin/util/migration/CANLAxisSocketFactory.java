/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.glite.security.voms.admin.util.migration;

import java.net.Socket;
import java.security.cert.X509Certificate;
import java.util.Hashtable;

import javax.net.SocketFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;

import org.apache.axis.components.net.BooleanHolder;
import org.apache.axis.components.net.SecureSocketFactory;
import org.italiangrid.voms.util.CertificateValidatorBuilder;

import eu.emi.security.authn.x509.impl.CertificateUtils;
import eu.emi.security.authn.x509.impl.HostnameMismatchCallback;

public class CANLAxisSocketFactory extends AbstractSocketFactory
    implements SecureSocketFactory, HostnameMismatchCallback, MigrateVoConstants {

  
  public CANLAxisSocketFactory(Hashtable table) {
    CertificateUtils.configureSecProvider();
    validator = new CertificateValidatorBuilder().build();
    proxyFile = System.getenv(X509_USER_PROXY_ENV);
  }

  @Override
  public Socket create(String host, int port, StringBuffer otherHeaders, BooleanHolder useFullURL)
      throws Exception {

    SocketFactory fac = createSocketFactory();

    SSLSocket socket = (SSLSocket) fac.createSocket(host, port);
    socket.setEnabledProtocols(new String[] {"TLSv1"});
    return socket;
  }



  @Override
  public void nameMismatch(SSLSocket socket, X509Certificate peerCertificate, String hostName)
      throws SSLException {
    // TODO Auto-generated method stub

  }

}
