/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
