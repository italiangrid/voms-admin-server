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

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.commons.lang.NotImplementedException;
import org.italiangrid.voms.util.CertificateValidatorBuilder;

import eu.emi.security.authn.x509.impl.CertificateUtils;

public class HttpClientSocketFactory extends AbstractSocketFactory
    implements SecureProtocolSocketFactory, MigrateVoConstants {

  SSLSocketFactory sslSocketFactory;
  
  public HttpClientSocketFactory() throws Exception {
    CertificateUtils.configureSecProvider();
    validator = new CertificateValidatorBuilder().build();
    proxyFile = System.getenv(X509_USER_PROXY_ENV);
   
    sslSocketFactory = createSocketFactory();
  }

  @Override
  public Socket createSocket(String host, int port, InetAddress localAddress, int localPort)
      throws IOException, UnknownHostException {
    SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(host, port, localAddress, localPort);
    socket.setEnabledProtocols(new String[] {"TLSv1"});
    return socket;
  }

  @Override
  public Socket createSocket(String host, int port, InetAddress localAddress, int localPort,
      HttpConnectionParams params)
      throws IOException, UnknownHostException, ConnectTimeoutException {
    
    return createSocket(host, port, localAddress, localPort);
  }

  @Override
  public Socket createSocket(String host, int port) throws IOException, UnknownHostException {

    SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(host, port);
    socket.setEnabledProtocols(new String[] {"TLSv1"});
    return socket;
  }

  @Override
  public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
      throws IOException, UnknownHostException {

    throw new NotImplementedException();
  }

}
