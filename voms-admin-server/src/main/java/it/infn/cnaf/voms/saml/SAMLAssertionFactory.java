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
/**************************************************************************

 Copyright 2006-2007 Istituto Nazionale di Fisica Nucleare (INFN)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 File : AssertionFactory.java

 Authors: Valerio Venturi <valerio.venturi@cnaf.infn.it>

 **************************************************************************/

package it.infn.cnaf.voms.saml;

import it.infn.cnaf.voms.aa.VOMSAA;
import it.infn.cnaf.voms.aa.VOMSAttributeAuthority;
import it.infn.cnaf.voms.aa.VOMSAttributes;
import it.infn.cnaf.voms.aa.VOMSFQAN;
import it.infn.cnaf.voms.saml.emi.AttributeWizard;
import it.infn.cnaf.voms.saml.emi.EMISAMLProfileConstants;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import javax.security.auth.x500.X500Principal;

import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.signature.XMLSignature;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.util.DNUtil;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.saml2.core.impl.ConditionsBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.NameIDBuilder;
import org.opensaml.saml2.core.impl.SubjectBuilder;
import org.opensaml.saml2.core.impl.SubjectConfirmationBuilder;
import org.opensaml.saml2.core.impl.SubjectConfirmationDataBuilder;
import org.opensaml.xml.Namespace;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorFactory;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorManager;
import org.opensaml.xml.security.x509.X509KeyInfoGeneratorFactory;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.impl.SignatureBuilder;

/**
 * @author Valerio Venturi <valerio.venturi@cnaf.infn.it>
 * @author Andrea Ceccanti <andrea.ceccanti@cnaf.infn.it>
 * 
 */
public class SAMLAssertionFactory {

  private X509Certificate certificate;

  private PrivateKey privateKey;

  public SAMLAssertionFactory(X509Certificate certificate, PrivateKey privateKey) {

    this.certificate = certificate;
    this.privateKey = privateKey;
  }

  protected Assertion createAssertion(X509Certificate subjectCertificate,
    List<Attribute> attributes, int lifetime) throws SecurityException {

    XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

    X500Principal subject = subjectCertificate.getSubjectX500Principal();

    AssertionBuilder assertionBuilder = (AssertionBuilder) builderFactory
      .getBuilder(org.opensaml.saml2.core.Assertion.DEFAULT_ELEMENT_NAME);

    Assertion assertion = assertionBuilder.buildObject();

    assertion.getNamespaceManager().registerNamespace(
      new Namespace(EMISAMLProfileConstants.DCI_SEC_NS,
        EMISAMLProfileConstants.DCI_SEC_PREFIX));

    /* set some attributes */

    assertion.setID("_" + UUID.randomUUID().toString());
    assertion.setIssueInstant(new DateTime());

    /* set Issuer */

    IssuerBuilder issuerBuilder = (IssuerBuilder) builderFactory
      .getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
    Issuer assertionIssuer = issuerBuilder.buildObject();
    assertionIssuer.setValue(certificate.getSubjectX500Principal().getName());

    assertionIssuer.setFormat(Issuer.X509_SUBJECT);
    assertion.setIssuer(assertionIssuer);

    /* set Subject */

    SubjectBuilder subjectBuilder = (SubjectBuilder) builderFactory
      .getBuilder(Subject.DEFAULT_ELEMENT_NAME);

    Subject assertionSubject = subjectBuilder.buildObject();

    NameIDBuilder nameIDBuilder = (NameIDBuilder) builderFactory
      .getBuilder(NameID.DEFAULT_ELEMENT_NAME);
    NameID nameID = nameIDBuilder.buildObject();
    nameID
      .setFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");
    nameID.setValue(subject.getName());

    assertionSubject.setNameID(nameID);

    assertion.setSubject(assertionSubject);

    /* set Conditions */

    ConditionsBuilder conditionsBuilder = (ConditionsBuilder) builderFactory
      .getBuilder(Conditions.DEFAULT_ELEMENT_NAME);
    Conditions conditions = conditionsBuilder.buildObject();
    conditions.setNotBefore(new DateTime());
    conditions.setNotOnOrAfter(new DateTime().plusMinutes(lifetime));
    assertion.setConditions(conditions);

    /* set AttributeStatement, i.e., encode VOMS attributes */

    AttributeStatementBuilder attributeStatementBuilder = (AttributeStatementBuilder) builderFactory
      .getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
    AttributeStatement attributeStatement = attributeStatementBuilder
      .buildObject();
    attributeStatement.getAttributes().addAll(attributes);

    assertion.getAttributeStatements().add(attributeStatement);

    /* set SubjectConfirmation */

    SubjectConfirmationBuilder subjectConfirmationBuilder = (SubjectConfirmationBuilder) builderFactory
      .getBuilder(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
    SubjectConfirmation subjectConfirmation = subjectConfirmationBuilder
      .buildObject();

    subjectConfirmation
      .setMethod("urn:oasis:names:tc:SAML:2.0:cm:holder-of-key");

    SubjectConfirmationDataBuilder subjectConfirmationDataBuilder = (SubjectConfirmationDataBuilder) builderFactory
      .getBuilder(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
    SubjectConfirmationData subjectConfirmationData = subjectConfirmationDataBuilder
      .buildObject();

    Credential peerCredential = SecurityHelper.getSimpleCredential(
      subjectCertificate, null);

    X509KeyInfoGeneratorFactory x509KeyInfoGeneratorFactory = new X509KeyInfoGeneratorFactory();
    x509KeyInfoGeneratorFactory.setEmitEntityCertificate(true);

    KeyInfoGeneratorManager keyInfoGeneratorManager = new KeyInfoGeneratorManager();
    keyInfoGeneratorManager.registerFactory(x509KeyInfoGeneratorFactory);

    KeyInfoGeneratorFactory keyInfoGeneratorFactory = keyInfoGeneratorManager
      .getFactory(peerCredential);
    KeyInfoGenerator keyInfoGenerator = keyInfoGeneratorFactory.newInstance();
    KeyInfo peerKeyInfo = keyInfoGenerator.generate(peerCredential);

    subjectConfirmationData.getUnknownXMLObjects().add(peerKeyInfo);

    subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);

    assertion.getSubject().getSubjectConfirmations().add(subjectConfirmation);

    Credential credential = SecurityHelper.getSimpleCredential(certificate,
      privateKey);

    SignatureBuilder signatureBuilder = (SignatureBuilder) builderFactory
      .getBuilder(Signature.DEFAULT_ELEMENT_NAME);
    Signature assertionSignature = signatureBuilder.buildObject();

    assertionSignature.setSigningCredential(credential);
    assertionSignature
      .setCanonicalizationAlgorithm(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
    assertionSignature
      .setSignatureAlgorithm(XMLSignature.ALGO_ID_SIGNATURE_RSA);

    KeyInfo serviceKeyInfo = keyInfoGenerator.generate(credential);

    assertionSignature.setKeyInfo(serviceKeyInfo);

    assertion.setSignature(assertionSignature);

    return assertion;
  }

  public Attribute findAttributeInQuery(AttributeQuery query,
    String attributeName) {

    for (Attribute a : query.getAttributes()) {
      if (a.getName().equals(attributeName))
        return a;

    }

    return null;
  }

  public Assertion create(X509Certificate subjectCertificate,
    AttributeQuery attributeQuery, int lifetime) throws SecurityException {

    String userDN = DNUtil.getBCasX500(attributeQuery.getSubject().getNameID()
      .getValue());

    List<Attribute> vomsSAMLAttributes = new ArrayList<Attribute>();

    VOMSAttributeAuthority vomsAA = VOMSAA.getVOMSAttributeAuthority();

    // The VOMS attributes, i.e., FQANs + Generic Attributes
    VOMSAttributes attributes = null;

    String voName = VOMSConfiguration.instance().getVOName();

    if (attributeQuery.getAttributes().isEmpty()) {

      attributes = vomsAA.getAllVOMSAttributes(userDN);
      vomsSAMLAttributes.addAll(SAMLAttributeSerializer
        .serializeAllAttributes(attributes));

      return createAssertion(subjectCertificate, vomsSAMLAttributes, lifetime);

    }

    // Some attributes were explicitly requested

    Attribute voAttr = findAttributeInQuery(attributeQuery,
      EMISAMLProfileConstants.VO_ATTRIBUTE_NAME);

    if (voAttr != null) {

      List<String> requestedVOs = AttributeWizard
        .attributeToListOfStrings(voAttr);

      if (requestedVOs.isEmpty() || requestedVOs.contains(voName))
        vomsSAMLAttributes.add(AttributeWizard.createVOAttribute(voName));

    }

    Attribute groupAttr = findAttributeInQuery(attributeQuery,
      EMISAMLProfileConstants.GROUP_ATTRIBUTE_NAME);

    if (groupAttr != null) {

      List<String> requestedGroups = AttributeWizard
        .attributeToListOfStrings(groupAttr);

      if (requestedGroups.isEmpty())
        attributes = vomsAA.getVOMSAttributes(userDN);
      else
        attributes = vomsAA.getVOMSAttributes(userDN, requestedGroups);

      vomsSAMLAttributes.add(AttributeWizard.createGroupAttribute(attributes
        .getFqans()));

      // Primary group is related to group
      Attribute pGroupAttr = findAttributeInQuery(attributeQuery,
        EMISAMLProfileConstants.GROUP_ATTRIBUTE_NAME);

      List<String> requestedPrimaryGroup = AttributeWizard
        .attributeToListOfStrings(pGroupAttr);

      if (requestedPrimaryGroup.isEmpty())
        vomsSAMLAttributes.add(AttributeWizard
          .createPrimaryGroupAttribute(attributes.getFqans().get(0)));
      else {

        VOMSFQAN reqPrimGroup = VOMSFQAN.fromString(requestedPrimaryGroup
          .get(0));

        if (attributes.getFqans().contains(reqPrimGroup))
          vomsSAMLAttributes.add(AttributeWizard
            .createPrimaryGroupAttribute(reqPrimGroup));

      }

      // Roles can be requested only if also groups are requested
      Attribute roleAttr = findAttributeInQuery(attributeQuery,
        EMISAMLProfileConstants.ROLE_ATTRIBUTE_NAME);

      if (roleAttr != null) {

        List<String> requestedRoles = AttributeWizard
          .roleAttributeToFQAN(roleAttr);
        VOMSAttributes roleAttrs = vomsAA.getVOMSAttributes(userDN,
          requestedRoles);

        ListIterator<VOMSFQAN> iter = roleAttrs.getFqans().listIterator();

        while (iter.hasNext()) {

          VOMSFQAN f = iter.next();
          if (!attributes.getFqans().contains(f.getGroupPartAsVOMSFQAN()))
            iter.remove();
        }

        if (roleAttrs.hasRoles()) {

          vomsSAMLAttributes.add(AttributeWizard.createRoleAttribute(roleAttrs
            .getFqans()));

          Attribute pRoleAttr = findAttributeInQuery(attributeQuery,
            EMISAMLProfileConstants.PRIMARY_ROLE_ATTRIBUTE_NAME);

          List<String> requestedPrimaryRole = AttributeWizard
            .roleAttributeToFQAN(pRoleAttr);

          if (requestedPrimaryRole.isEmpty())
            vomsSAMLAttributes.add(AttributeWizard
              .createPrimaryRoleAttribute(roleAttrs.getFqans().get(0)));
          else {

            VOMSFQAN reqPrimRole = VOMSFQAN.fromString(requestedPrimaryRole
              .get(0));
            if (roleAttrs.getFqans().contains(reqPrimRole))
              vomsSAMLAttributes.add(AttributeWizard
                .createPrimaryRoleAttribute(reqPrimRole));
          }

        }

      }
    }

    return createAssertion(subjectCertificate, vomsSAMLAttributes, lifetime);
  }

}
