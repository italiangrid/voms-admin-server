/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
