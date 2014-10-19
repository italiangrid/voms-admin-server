package org.italiangrid.voms.container.legacy;

import org.eclipse.jetty.io.Buffer;

public interface JettyHTTPGetRequestBuffers {

  public Buffer getMethodBuffer();

  public Buffer getURIBuffer();

  public Buffer getVersionBuffer();

  public void clearBuffers();
}
