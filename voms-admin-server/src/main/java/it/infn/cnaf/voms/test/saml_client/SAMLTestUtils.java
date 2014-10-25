/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

package it.infn.cnaf.voms.test.saml_client;

import it.infn.cnaf.voms.saml.axis_serializers.DeserializerFactory;
import it.infn.cnaf.voms.saml.axis_serializers.SerializerFactory;
import it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityPortType;
import it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityServiceLocator;

import java.net.URL;
import java.util.UUID;

import javax.xml.rpc.encoding.TypeMapping;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.glite.security.voms.service.admin.VOMSAdmin;
import org.glite.security.voms.service.admin.VOMSAdminServiceLocator;
import org.glite.security.voms.service.attributes.VOMSAttributes;
import org.glite.security.voms.service.attributes.VOMSAttributesServiceLocator;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.impl.AttributeQueryBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.opensaml.saml2.core.impl.SubjectBuilder;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.w3c.dom.Element;

import eu.emi.security.authn.x509.impl.OpensslNameUtils;

public class SAMLTestUtils {

  public static VOMSAdmin getVOMSAdminService(String host, String vo)
    throws Exception {

    String url = String.format("https://%s:8443/voms/%s/services/VOMSAdmin",
      host, vo);

    VOMSAdminServiceLocator loc = new VOMSAdminServiceLocator();
    return loc.getVOMSAdmin(new URL(url));

  }

  public static VOMSAttributes getVOMSAttributesService(String host, String vo)
    throws Exception {

    String url = String.format(
      "https://%s:8443/voms/%s/services/VOMSAttributes", host, vo);

    VOMSAttributesServiceLocator loc = new VOMSAttributesServiceLocator();
    return loc.getVOMSAttributes(new URL(url));

  }

  public static AttributeAuthorityPortType getVOMSSAMLService(String host,
    String vo) throws Exception {

    String url = String.format("https://%s:8443/voms/%s/services/VOMSSaml",
      host, vo);

    AttributeAuthorityServiceLocator loc = new AttributeAuthorityServiceLocator();
    TypeMapping typeMapping = loc.getTypeMappingRegistry()
      .getDefaultTypeMapping();

    typeMapping.register(AttributeQuery.class, AttributeQuery.TYPE_NAME,
      new SerializerFactory(), new DeserializerFactory());

    typeMapping.register(Response.class, Response.TYPE_NAME,
      new SerializerFactory(), new DeserializerFactory());

    AttributeAuthorityPortType aa = loc.getAttributeAuthorityPortType(new URL(
      url));

    return aa;

  }

  public static AttributeQuery buildAttributeQuery(String userDn, String voName)
    throws Exception {

    XMLObjectBuilderFactory bf = Configuration.getBuilderFactory();

    AttributeQueryBuilder qb = (AttributeQueryBuilder) bf
      .getBuilder(AttributeQuery.DEFAULT_ELEMENT_NAME);

    AttributeQuery query = qb.buildObject();

    query.setID(UUID.randomUUID().toString());
    query.setVersion(SAMLVersion.VERSION_20);

    query.setIssueInstant(new DateTime());

    IssuerBuilder issuerBuilder = (IssuerBuilder) bf
      .getBuilder(Issuer.DEFAULT_ELEMENT_NAME);

    Issuer issuer = issuerBuilder.buildObject();

    // Convert to rfc2253
    String opensslDn = OpensslNameUtils.opensslToRfc2253(userDn);

    issuer.setValue(opensslDn);
    issuer.setFormat(NameID.X509_SUBJECT);

    query.setIssuer(issuer);

    SubjectBuilder subjectBuilder = (SubjectBuilder) bf
      .getBuilder(Subject.DEFAULT_ELEMENT_NAME);
    Subject subject = subjectBuilder.buildObject();

    NameIDBuilder nameIdBuilder = (NameIDBuilder) bf
      .getBuilder(NameID.DEFAULT_ELEMENT_NAME);

    NameID requester = nameIdBuilder.buildObject();
    requester.setFormat(NameID.X509_SUBJECT);
    requester.setValue(opensslDn);

    subject.setNameID(requester);

    query.setSubject(subject);

    return query;
  }

  public static void printXMLObject(XMLObject xmlObject) throws Exception {

    Element element;

    MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();

    Marshaller marshaller = marshallerFactory.getMarshaller(xmlObject);
    element = marshaller.marshall(xmlObject);

    Transformer tr = TransformerFactory.newInstance().newTransformer();

    tr.setOutputProperty(OutputKeys.INDENT, "yes");
    tr.setOutputProperty(OutputKeys.METHOD, "xml");

    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
      String.valueOf(4));

    tr.transform(new DOMSource(element), new StreamResult(System.out));

  }

}
