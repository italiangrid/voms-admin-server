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
