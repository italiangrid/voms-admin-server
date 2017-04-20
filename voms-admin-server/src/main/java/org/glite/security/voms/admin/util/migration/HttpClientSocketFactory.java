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
