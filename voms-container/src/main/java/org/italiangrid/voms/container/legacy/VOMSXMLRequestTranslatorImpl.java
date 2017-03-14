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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.eclipse.jetty.io.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VOMSXMLRequestTranslatorImpl implements VOMSXMLRequestTranslator {

  public static final Logger log = LoggerFactory
    .getLogger(VOMSXMLRequestTranslatorImpl.class);

  List<String> fqans = new ArrayList<String>();
  List<String> targets = new ArrayList<String>();
  String lifetime;
  String orderString;

  private static final XMLInputFactory factory = XMLInputFactory.newInstance();

  private void parseBCommand(String command) {

    int roleIndex = command.indexOf(':');
    StringBuilder fqanBuilder = new StringBuilder();
    fqanBuilder.append(command.substring(1, roleIndex));
    fqanBuilder.append("/Role=");
    fqanBuilder.append(command.substring(roleIndex + 1));
    fqans.add(fqanBuilder.toString());
  }

  private void parseCommand(String command) {

    if (command.startsWith("G"))
      fqans.add(command.substring(1));
    else if (command.startsWith("B"))
      parseBCommand(command);
  }

  private void parseLifetime(String lifetime) {

    this.lifetime = lifetime;
  }

  public void parseRequest(Buffer requestBuffer) throws XMLStreamException,
    IOException {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    requestBuffer.writeTo(baos);

    XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(
      baos.toString()));

    String tagContent = null;

    while (reader.hasNext()) {
      int event = reader.next();
      switch (event) {

      case XMLStreamConstants.CHARACTERS:
        tagContent = reader.getText().trim();
        break;

      case XMLStreamConstants.END_ELEMENT:
        String elementName = reader.getLocalName();
        if (elementName.equals("command"))
          parseCommand(tagContent);
        if (elementName.equals("lifetime"))
          parseLifetime(tagContent);
        break;
      }
    }
  }

  private void buildURI(JettyHTTPGetRequestBuffers buffers) {

    Buffer uriBuffer = buffers.getURIBuffer();
    uriBuffer.clear();
    uriBuffer.put("/generate-ac?fqans=".getBytes());
    boolean firstFqan = true;

    for (String f : fqans) {
      if (!firstFqan)
        uriBuffer.put((byte) ',');
      uriBuffer.put(f.getBytes());
      firstFqan = false;
    }

    String lifetimeFrag = String.format("&lifetime=%s", lifetime);
    uriBuffer.put(lifetimeFrag.getBytes());
  }

  @Override
  public boolean translateLegacyRequest(Buffer requestBuffer,
    JettyHTTPGetRequestBuffers outputBuffers) {

    try {
      parseRequest(requestBuffer);
    } catch (Throwable t) {

      log.debug("Error parsing VOMS legacy request: {}. {}", new Object[] {
        requestBuffer.toString(), t }, t);

      return false;
    }

    buildURI(outputBuffers);
    return true;
  }
}