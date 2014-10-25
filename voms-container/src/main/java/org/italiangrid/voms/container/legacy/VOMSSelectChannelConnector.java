package org.italiangrid.voms.container.legacy;

import java.nio.channels.SocketChannel;

import org.eclipse.jetty.io.AsyncEndPoint;
import org.eclipse.jetty.io.nio.AsyncConnection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

public class VOMSSelectChannelConnector extends SelectChannelConnector {

  @Override
  protected AsyncConnection newConnection(SocketChannel channel,
    AsyncEndPoint endpoint) {

    return new VOMSConnection(VOMSSelectChannelConnector.this, endpoint,
      getServer());
  }

}
