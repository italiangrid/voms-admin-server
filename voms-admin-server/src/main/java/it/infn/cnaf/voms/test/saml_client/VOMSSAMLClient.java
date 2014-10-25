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
import it.infn.cnaf.voms.saml.emi.AttributeWizard;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.encoding.TypeMapping;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.impl.AttributeQueryBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.opensaml.saml2.core.impl.SubjectBuilder;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

public class VOMSSAMLClient {

  static final String MY_DN = "CN=Andrea Ceccanti,L=CNAF,OU=Personal Certificate,O=INFN,C=IT";
  static final String MY_OTHER_DN = "emailAddress=andrea.ceccanti@cnaf.infn.it,CN=cecco,OU=Voms-Admin testing,O=Voms-Admin,ST=Test,C=IT";

  static {

    Security.addProvider(new BouncyCastleProvider());

  }

  public static void main(String[] args) throws MalformedURLException,
    ServiceException, RemoteException {

    new VOMSSAMLClient(args);
  }

  public static Element marshall(XMLObject xmlObject)
    throws MarshallingException {

    Element element;

    MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();
    Marshaller marshaller = marshallerFactory.getMarshaller(xmlObject);
    element = marshaller.marshall(xmlObject);

    return element;
  }

  public void usage() {

    System.out.format("usage: %s <host> <vo> <dn>", this.getClass().getName());

  }

  public void testEmptyAttributeQuery() {

  }

  public VOMSSAMLClient(String[] args) throws MalformedURLException,
    ServiceException, RemoteException {

    if (args.length < 3) {
      usage();
      System.exit(-1);

    }

    String host = args[0];
    String vo = args[1];
    String dn = args[2];

    initializeOpenSAML();

    List<String> fqans = new ArrayList<String>();

    if (args.length > 2) {

      for (int i = 2; i < args.length; i++)
        fqans.add(args[i]);

    }

    AttributeQuery query = buildAttributeQuery(MY_DN, vo, fqans);

    System.out.println("Query:");
    print(query);

    AttributeAuthorityPortType aa = getVOMSSamlService(host, vo);

    Response response = aa.attributeQuery(query);

    System.out.println("Response:");
    print(response);

  }

  AttributeQuery buildAttributeQuery(String userDn, String voName,
    List<String> fqans) {

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
    issuer.setValue(userDn);
    issuer.setFormat(NameID.X509_SUBJECT);

    query.setIssuer(issuer);

    SubjectBuilder subjectBuilder = (SubjectBuilder) bf
      .getBuilder(Subject.DEFAULT_ELEMENT_NAME);
    Subject subject = subjectBuilder.buildObject();

    NameIDBuilder nameIdBuilder = (NameIDBuilder) bf
      .getBuilder(NameID.DEFAULT_ELEMENT_NAME);

    NameID requester = nameIdBuilder.buildObject();
    requester.setFormat(NameID.X509_SUBJECT);
    requester.setValue(userDn);

    subject.setNameID(requester);

    query.setSubject(subject);

    if (!fqans.isEmpty()) {
      List<Attribute> requestedAttrs = new ArrayList<Attribute>();

      requestedAttrs.add(AttributeWizard.createVOAttribute(voName));

      List<String> groups = new ArrayList<String>();
      List<String> roles = new ArrayList<String>();

      for (String f : fqans) {

        if (PathNamingScheme.isGroupFQAN(f))
          groups.add(f);
        else
          roles.add(f);
      }

      requestedAttrs.add(AttributeWizard
        .createPrimaryGroupAttributeFromString(null));

      requestedAttrs.add(AttributeWizard
        .createGroupAttributeFromStrings(groups));

      if (!roles.isEmpty()) {
        requestedAttrs.add(AttributeWizard
          .createRoleAttributeFromStrings(roles));
        requestedAttrs.add(AttributeWizard
          .createPrimaryRoleAttributeFromString(null));
      }

      query.getAttributes().addAll(requestedAttrs);
    }

    return query;
  }

  AttributeAuthorityPortType getVOMSSamlService(String host, String vo) {

    String url = String.format("https://%s:8443/voms/%s/services/VOMSSaml",
      host, vo);

    try {

      AttributeAuthorityServiceLocator loc = new AttributeAuthorityServiceLocator();
      TypeMapping typeMapping = loc.getTypeMappingRegistry()
        .getDefaultTypeMapping();

      typeMapping.register(AttributeQuery.class, AttributeQuery.TYPE_NAME,
        new SerializerFactory(), new DeserializerFactory());

      typeMapping.register(Response.class, Response.TYPE_NAME,
        new SerializerFactory(), new DeserializerFactory());

      AttributeAuthorityPortType aa = loc
        .getAttributeAuthorityPortType(new URL(url));

      return aa;

    } catch (Throwable t) {
      printExceptionAndExit(t);

    }

    return null;
  }

  void initializeOpenSAML() {

    try {

      DefaultBootstrap.bootstrap();

    } catch (ConfigurationException e) {

      printExceptionAndExit(e);
    }
  }

  void parseCredentials(String certFile, String keyFile) {

  }

  public void print(XMLObject xmlObject) {

    try {

      Element element = marshall(xmlObject);

      Transformer tr = TransformerFactory.newInstance().newTransformer();
      tr.setOutputProperty(OutputKeys.INDENT, "yes");
      tr.setOutputProperty(OutputKeys.METHOD, "xml");
      tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
        String.valueOf(4));
      tr.transform(new DOMSource(element), new StreamResult(System.out));

    } catch (Throwable t) {

      printExceptionAndExit(t);

    }

  }

  public void printExceptionAndExit(Throwable t) {

    t.printStackTrace();
    System.exit(1);

  }
}
