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
package org.italiangrid.voms.aa.x509.impl;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.glite.security.voms.admin.error.VOMSException;
import org.italiangrid.voms.aa.VOMSErrorMessage;
import org.italiangrid.voms.aa.VOMSWarningMessage;
import org.italiangrid.voms.aa.x509.VOMSResponseBuilder;
import org.opensaml.xml.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

public enum VOMSResponseBuilderImpl implements VOMSResponseBuilder {

  INSTANCE;

  private final Logger log = LoggerFactory
    .getLogger(VOMSResponseBuilderImpl.class);

  protected DocumentBuilder docBuilder;

  private TransformerFactory transformerFactory = TransformerFactory
    .newInstance();

  private VOMSResponseBuilderImpl() {

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    factory.setIgnoringComments(true);
    factory.setNamespaceAware(false);
    factory.setValidating(false);

    try {

      docBuilder = factory.newDocumentBuilder();

    } catch (ParserConfigurationException e) {
      log.error("Error configuring DOM document builder: " + e.getMessage(), e);
      throw new VOMSException(e);
    }

  }

  protected String xmlDocAsString(Document doc) {

    Transformer transformer;

    try {
      transformer = transformerFactory.newTransformer();
    } catch (TransformerConfigurationException e) {

      log.error("Error creating XML transformer:" + e.getMessage(), e);
      throw new VOMSException(e);

    }

    StringWriter writer = new StringWriter();

    DOMSource source = new DOMSource(doc);
    StreamResult res = new StreamResult(writer);

    try {

      transformer.transform(source, res);
      writer.flush();

    } catch (TransformerException e) {

      log.error("Error caught serializing XML :" + e.getMessage());
      if (log.isDebugEnabled())
        log.error(e.getMessage(), e);

      throw new VOMSException("Error caugh serializing XML :", e);

    }
    String output = writer.toString();
    log.debug("Serialized: {}", output);
    return output;
  }

  @Override
  public String createResponse(byte[] acBytes, List<VOMSWarningMessage> warnings) {

    Document response = docBuilder.newDocument();
    VOMSResponseFragment frag = new VOMSResponseFragment(response);

    frag.buildACElement(Base64.encodeBytes(acBytes), warnings);
    response.appendChild(frag.getFragment());

    return xmlDocAsString(response);
  }

  @Override
  public String createErrorResponse(VOMSErrorMessage errorMessage) {

    Document response = docBuilder.newDocument();
    VOMSResponseFragment frag = new VOMSResponseFragment(response);

    frag.buildErrorElement(errorMessage);
    response.appendChild(frag.getFragment());

    return xmlDocAsString(response);
  }

  @Override
  public String createLegacyErrorResponse(VOMSErrorMessage errorMessage) {

    Document response = docBuilder.newDocument();
    VOMSResponseFragment frag = new VOMSResponseFragment(response);

    frag.buildLegacyErrorElement(errorMessage);
    response.appendChild(frag.getFragment());

    return xmlDocAsString(response);
  }
}

class VOMSResponseFragment {

  private Document doc;
  DocumentFragment fragment;

  VOMSResponseFragment(Document document) {

    this.doc = document;
    fragment = doc.createDocumentFragment();

  }

  void buildACElement(String base64EncodedACString,
    List<VOMSWarningMessage> warnings) {

    Element root = doc.createElement("voms");
    fragment.appendChild(root);

    Element ac = doc.createElement("ac");
    appendTextChild(ac, base64EncodedACString);
    root.appendChild(ac);

    for (VOMSWarningMessage w : warnings) {
      Element warningElement = doc.createElement("warning");
      String warningMessage = String.format("WARNING: %s : %s", w.getVo(),
        w.getMessage());
      appendTextChild(warningElement, warningMessage);
      root.appendChild(warningElement);
    }

  }

  void buildLegacyErrorElement(VOMSErrorMessage m) {

    Element root = doc.createElement("vomsans");
    Element error = doc.createElement("error");
    Element errorItem = doc.createElement("item");
    Element errorItemNumber = doc.createElement("number");
    Element errorItemMessage = doc.createElement("message");
    Element ac = doc.createElement("ac");
    Element version = doc.createElement("version");

    appendTextChild(errorItemNumber,
      Integer.toString(m.getError().getLegacyErrorCode()));

    appendTextChild(errorItemMessage, m.getMessage());

    appendTextChild(version, "3");

    // This nonsense is needed so that legacy voms-clients correctly parse the
    // generated response and report errors as expected.
    appendTextChild(ac, "QQ==\n");

    root.appendChild(version);
    root.appendChild(error);
    root.appendChild(ac);

    error.appendChild(errorItem);
    errorItem.appendChild(errorItemNumber);
    errorItem.appendChild(errorItemMessage);

    fragment.appendChild(root);
  }

  void buildErrorElement(VOMSErrorMessage m) {

    Element root = doc.createElement("voms");
    Element error = doc.createElement("error");
    Element errorCodeElement = doc.createElement("code");
    Element errorMessageElement = doc.createElement("message");

    appendTextChild(errorMessageElement, m.getMessage());

    appendTextChild(errorCodeElement,
      Integer.toString(m.getError().getLegacyErrorCode()));

    error.appendChild(errorCodeElement);
    error.appendChild(errorMessageElement);

    root.appendChild(error);
    fragment.appendChild(root);
  }

  DocumentFragment getFragment() {

    return fragment;
  }

  private void appendTextChild(Element e, String text) {

    e.appendChild(doc.createTextNode(text));
  }

}
