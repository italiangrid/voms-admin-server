package org.italiangrid.voms.container.legacy;

import org.eclipse.jetty.io.Buffer;

/**
 * Translates a legacy VOMS XML request into an HTTP VOMS request.
 *
 */
public interface VOMSXMLRequestTranslator {

  /**
   * Parses the legacy VOMS request contained in the requestBuffer and places
   * the output in the output buffers.
   * 
   * @param requestBuffer
   *          the buffer containing the incoming legacy XML VOMS request
   * 
   * @param outputBuffers
   *          the buffers where the translated HTTP VOMS request will be placed.
   * 
   * @return <code>true</code> if the parsing is successfull, <code>false</code>
   *         otherwise
   */
  public boolean translateLegacyRequest(Buffer requestBuffer,
    JettyHTTPGetRequestBuffers outputBuffers);

}
