package org.italiangrid.voms.container.legacy;

import java.io.IOException;

import org.eclipse.jetty.http.HttpGenerator;
import org.eclipse.jetty.io.Buffers;
import org.eclipse.jetty.io.EndPoint;

public class VOMSGenerator extends HttpGenerator {

  private final VOMSConnection _connection;

  public VOMSGenerator(VOMSConnection conn, Buffers buffers, EndPoint io) {

    super(buffers, io);
    _connection = conn;
  }

  protected boolean isLegacyRequest() {

    return (_connection.getRequest().getHeader(
      LegacyHTTPHeader.LEGACY_REQUEST_HEADER.getHeaderName()) != null);
  }

  @Override
  public int flushBuffer() throws IOException {

    if (!isLegacyRequest()) {
      return super.flushBuffer();
    }

    int numWritten = _endp.flush(_buffer);
    _state = STATE_END;
    _endp.shutdownOutput();

    return numWritten;
  }

}
