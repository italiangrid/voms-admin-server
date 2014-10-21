package org.italiangrid.voms.container.legacy;

import org.eclipse.jetty.http.HttpGenerator;
import org.eclipse.jetty.http.HttpParser;
import org.eclipse.jetty.http.HttpParser.EventHandler;
import org.eclipse.jetty.io.Buffers;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.server.AsyncHttpConnection;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;

/**
 * A VOMSConnection provides a different HTTP Parser that translates incoming
 * VOMS legacy requests into HTTP requests.
 * 
 * @author andreaceccanti
 */
public class VOMSConnection extends AsyncHttpConnection {

  public VOMSConnection(Connector connector, EndPoint endpoint, Server server) {

    super(connector, endpoint, server);
  }

  @Override
  protected HttpParser newHttpParser(Buffers requestBuffers, EndPoint endpoint,
    EventHandler requestHandler) {

    return new VOMSParser(requestBuffers, endpoint, requestHandler);
  }

  @Override
  protected HttpGenerator newHttpGenerator(Buffers responseBuffers,
    EndPoint endPoint) {

    return new VOMSGenerator(this, responseBuffers, endPoint);
  }
}
