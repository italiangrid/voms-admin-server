package org.italiangrid.voms.container.legacy;

import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.nio.IndirectNIOBuffer;

public class HTTPRequestBuffers implements JettyHTTPGetRequestBuffers {

  private static final Buffer methodBuffer;
  private static final Buffer versionBuffer;
  private final Buffer urlBuffer = new IndirectNIOBuffer(2048);

  static {
    methodBuffer = new IndirectNIOBuffer(3);
    methodBuffer.put("GET".getBytes());
    versionBuffer = new IndirectNIOBuffer(8);
    versionBuffer.put("HTTP/1.0".getBytes());
  }

  public HTTPRequestBuffers() {

  }

  @Override
  public Buffer getMethodBuffer() {

    return methodBuffer;
  }

  @Override
  public Buffer getURIBuffer() {

    return urlBuffer;
  }

  @Override
  public Buffer getVersionBuffer() {

    return versionBuffer;
  }

  @Override
  public void clearBuffers() {

    urlBuffer.clear();
  }

  @Override
  public String toString() {

    return String.format("%s %s %s", methodBuffer.toString(),
      urlBuffer.toString(), versionBuffer.toString());
  }
}
