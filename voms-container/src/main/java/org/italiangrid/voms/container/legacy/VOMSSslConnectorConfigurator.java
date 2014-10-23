package org.italiangrid.voms.container.legacy;

import javax.net.ssl.SSLContext;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.italiangrid.utils.https.JettySSLConnectorConfigurator;
import org.italiangrid.utils.https.SSLOptions;
import org.italiangrid.utils.https.impl.canl.CANLListener;
import org.italiangrid.voms.util.CertificateValidatorBuilder;

import eu.emi.security.authn.x509.X509CertChainValidatorExt;
import eu.emi.security.authn.x509.impl.PEMCredential;
import eu.emi.security.authn.x509.impl.SocketFactoryCreator;

public class VOMSSslConnectorConfigurator implements
  JettySSLConnectorConfigurator {

  X509CertChainValidatorExt certChainValidator;
  PEMCredential serviceCredential;

  public VOMSSslConnectorConfigurator(X509CertChainValidatorExt validator,
    PEMCredential cred) {

    this.certChainValidator = validator;
    this.serviceCredential = cred;
  }

  public VOMSSslConnectorConfigurator(X509CertChainValidatorExt validator) {

    this(validator, null);
  }

  public VOMSSslConnectorConfigurator() {

  }

  @Override
  public Connector configureConnector(String host, int port, SSLOptions options) {

    Connector connector;

    try {

      if (serviceCredential == null) {
        serviceCredential = new PEMCredential(options.getKeyFile(),
          options.getCertificateFile(), options.getKeyPassword());
      }

      if (certChainValidator == null) {
        CANLListener l = new CANLListener();

        certChainValidator = new CertificateValidatorBuilder()
          .trustAnchorsDir(options.getTrustStoreDirectory())
          .trustAnchorsUpdateInterval(
            options.getTrustStoreRefreshIntervalInMsec())
          .storeUpdateListener(l).validationErrorListener(l).build();
      }

      SSLContext sslContext = SocketFactoryCreator.getSSLContext(
        serviceCredential, certChainValidator, null);

      SslContextFactory factory = new SslContextFactory();
      factory.setSslContext(sslContext);
      factory.setWantClientAuth(options.isWantClientAuth());
      factory.setNeedClientAuth(options.isNeedClientAuth());

      connector = new VOMSSslSelectChannelConnector(factory);
      connector.setHost(host);
      connector.setPort(port);

      return connector;

    } catch (Throwable t) {

      throw new RuntimeException(t);
    }
  }

}
