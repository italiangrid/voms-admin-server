package org.italiangrid.voms.container.legacy;

import java.nio.channels.SocketChannel;

import org.eclipse.jetty.io.AsyncEndPoint;
import org.eclipse.jetty.io.nio.AsyncConnection;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class VOMSSslSelectChannelConnector extends SslSelectChannelConnector {

  public VOMSSslSelectChannelConnector() {

    super();
  }

  public VOMSSslSelectChannelConnector(SslContextFactory sslContextFactory) {

    super(sslContextFactory);
  }

  @Override
  protected AsyncConnection newPlainConnection(SocketChannel channel,
    AsyncEndPoint endPoint) {

    return new VOMSConnection(VOMSSslSelectChannelConnector.this, endPoint,
      getServer());
  }
}
