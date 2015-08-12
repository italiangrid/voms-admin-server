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
package it.infn.cnaf.voms.saml.axis_serializers;

import java.io.IOException;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.Constants;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.wsdl.fromJava.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.Signer;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;

/**
 * @author Valerio Venturi (valerio.venturi@cnaf.infn.it)
 * 
 */
public class Serializer implements org.apache.axis.encoding.Serializer {

  static Logger logger = LoggerFactory.getLogger(Serializer.class);

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.apache.axis.encoding.Serializer#serialize(javax.xml.namespace.QName,
   * org.xml.sax.Attributes, java.lang.Object,
   * org.apache.axis.encoding.SerializationContext)
   */
  public void serialize(QName name, Attributes attributes, Object value,
    SerializationContext context) throws IOException {

    try {
      if (!(value instanceof XMLObject))
        throw new IOException("Can't serialize a " + value.getClass().getName()
          + ", " + XMLObject.class + "expected.");

      XMLObject xmlObject = (XMLObject) value;

      /* call OpenSAML serializing */

      MarshallerFactory marshallerFactory = Configuration
        .getMarshallerFactory();
      Marshaller marshaller = marshallerFactory.getMarshaller(xmlObject);
      Element element = marshaller.marshall(xmlObject);

      /* when a response, compute the signature value */

      if (value instanceof Response) {
        Response response = (Response) value;
        List<Assertion> assertions = response.getAssertions();
        if (assertions.size() > 0) {
          Assertion assertion = assertions.get(0);
          if (assertion != null) {
            Signature signature = assertion.getSignature();
            if (signature != null)
              Signer.signObject(signature);
          }
        }
      }

      /* */

      if (attributes != null) {
        for (int i = 0; i < attributes.getLength(); i++) {
          element.setAttributeNS(attributes.getURI(i), attributes.getQName(i),
            attributes.getValue(i));
        }
      }

      context.setWriteXMLType(null);
      context.writeDOMElement((Element) element);
    } catch (Exception exception) {
      throw new IOException("Error serializing " + value.getClass().getName()
        + " : " + exception.getClass().getName());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.axis.encoding.Serializer#writeSchema(java.lang.Class,
   * org.apache.axis.wsdl.fromJava.Types)
   */
  public Element writeSchema(Class javaType, Types types) throws Exception {

    Element complexType = types.createElement("complexType");
    return complexType;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.xml.rpc.encoding.Serializer#getMechanismType()
   */
  public String getMechanismType() {

    // TODO Auto-generated method stub
    return Constants.AXIS_SAX;
  }

}
