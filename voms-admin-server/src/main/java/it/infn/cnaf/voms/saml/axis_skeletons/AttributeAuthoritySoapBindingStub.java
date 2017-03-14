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
/**
 * AttributeAuthoritySoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.infn.cnaf.voms.saml.axis_skeletons;

public class AttributeAuthoritySoapBindingStub extends
  org.apache.axis.client.Stub implements
  it.infn.cnaf.voms.saml.axis_skeletons.AttributeAuthorityPortType {

  private java.util.Vector cachedSerClasses = new java.util.Vector();
  private java.util.Vector cachedSerQNames = new java.util.Vector();
  private java.util.Vector cachedSerFactories = new java.util.Vector();
  private java.util.Vector cachedDeserFactories = new java.util.Vector();

  static org.apache.axis.description.OperationDesc[] _operations;

  static {
    _operations = new org.apache.axis.description.OperationDesc[1];
    _initOperationDesc1();
  }

  private static void _initOperationDesc1() {

    org.apache.axis.description.OperationDesc oper;
    org.apache.axis.description.ParameterDesc param;
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("method");
    param = new org.apache.axis.description.ParameterDesc(
      new javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
        "AttributeQuery"), org.apache.axis.description.ParameterDesc.IN,
      new javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
        "AttributeQueryType"), org.opensaml.saml2.core.AttributeQuery.class,
      false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
      "urn:oasis:names:tc:SAML:2.0:protocol", "ResponseType"));
    oper.setReturnClass(org.opensaml.saml2.core.Response.class);
    oper.setReturnQName(new javax.xml.namespace.QName(
      "urn:oasis:names:tc:SAML:2.0:protocol", "Response"));
    oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
    oper.setUse(org.apache.axis.constants.Use.LITERAL);
    _operations[0] = oper;

  }

  public AttributeAuthoritySoapBindingStub() throws org.apache.axis.AxisFault {

    this(null);
  }

  public AttributeAuthoritySoapBindingStub(java.net.URL endpointURL,
    javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {

    this(service);
    super.cachedEndpoint = endpointURL;
  }

  public AttributeAuthoritySoapBindingStub(javax.xml.rpc.Service service)
    throws org.apache.axis.AxisFault {

    if (service == null) {
      super.service = new org.apache.axis.client.Service();
    } else {
      super.service = service;
    }
    ((org.apache.axis.client.Service) super.service)
      .setTypeMappingVersion("1.2");
    java.lang.Class cls;
    javax.xml.namespace.QName qName;
    javax.xml.namespace.QName qName2;
    java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
    java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
    java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
    java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
    java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
    java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
    java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
    java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
    java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
    java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;

    /*
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "CanonicalizationMethodType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.CanonicalizationMethodType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "CryptoBinary"); cachedSerQNames.add(qName); cls = byte[].class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(arraysf);
     * cachedDeserFactories.add(arraydf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "DigestMethodType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.DigestMethodType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "DigestValueType"); cachedSerQNames.add(qName); cls = byte[].class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(arraysf);
     * cachedDeserFactories.add(arraydf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "DSAKeyValueType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.DSAKeyValueType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "HMACOutputLengthType"); cachedSerQNames.add(qName); cls =
     * java.math.BigInteger.class; cachedSerClasses.add(cls);
     * cachedSerFactories.
     * add(org.apache.axis.encoding.ser.BaseSerializerFactory.
     * createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
     * cls, qName));
     * cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
     * .
     * createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class
     * , cls, qName));
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "KeyInfoType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.KeyInfoType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "KeyValueType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.KeyValueType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "ManifestType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.ReferenceType[].class;
     * cachedSerClasses.add(cls); qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "Reference"); qName2 = null; cachedSerFactories.add(new
     * org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
     * cachedDeserFactories.add(new
     * org.apache.axis.encoding.ser.ArrayDeserializerFactory());
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "ObjectType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.ObjectType.class; cachedSerClasses.add(cls);
     * cachedSerFactories.add(beansf); cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "PGPDataType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.PGPDataType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "ReferenceType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.ReferenceType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "RetrievalMethodType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.RetrievalMethodType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "RSAKeyValueType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.RSAKeyValueType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "SignatureMethodType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.SignatureMethodType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "SignaturePropertiesType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.SignaturePropertyType[].class;
     * cachedSerClasses.add(cls); qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "SignatureProperty"); qName2 = null; cachedSerFactories.add(new
     * org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
     * cachedDeserFactories.add(new
     * org.apache.axis.encoding.ser.ArrayDeserializerFactory());
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "SignaturePropertyType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.SignaturePropertyType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "SignatureType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.SignatureType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "SignatureValueType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.SignatureValueType.class;
     * cachedSerClasses.add(cls);
     * cachedSerFactories.add(org.apache.axis.encoding
     * .ser.BaseSerializerFactory.
     * createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
     * cls, qName));
     * cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
     * .
     * createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class
     * , cls, qName));
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "SignedInfoType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.SignedInfoType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "SPKIDataType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.SPKIDataType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "TransformsType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.TransformType[].class;
     * cachedSerClasses.add(cls); qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "Transform"); qName2 = null; cachedSerFactories.add(new
     * org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
     * cachedDeserFactories.add(new
     * org.apache.axis.encoding.ser.ArrayDeserializerFactory());
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "TransformType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.TransformType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "X509DataType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.X509DataType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "X509IssuerSerialType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.X509IssuerSerialType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * ">ReferenceList"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2001._04.xmlenc.ReferenceList.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "AgreementMethodType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2001._04.xmlenc.AgreementMethodType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "CipherDataType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2001._04.xmlenc.CipherDataType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "CipherReferenceType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2001._04.xmlenc.CipherReferenceType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "EncryptedDataType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2001._04.xmlenc.EncryptedDataType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "EncryptedKeyType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2001._04.xmlenc.EncryptedKeyType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "EncryptedType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2001._04.xmlenc.EncryptedType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "EncryptionMethodType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2001._04.xmlenc.EncryptionMethodType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "EncryptionPropertiesType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2001._04.xmlenc.EncryptionPropertyType[].class;
     * cachedSerClasses.add(cls); qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "EncryptionProperty"); qName2 = null; cachedSerFactories.add(new
     * org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
     * cachedDeserFactories.add(new
     * org.apache.axis.encoding.ser.ArrayDeserializerFactory());
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "EncryptionPropertyType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2001._04.xmlenc.EncryptionPropertyType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "KeySizeType"); cachedSerQNames.add(qName); cls =
     * java.math.BigInteger.class; cachedSerClasses.add(cls);
     * cachedSerFactories.
     * add(org.apache.axis.encoding.ser.BaseSerializerFactory.
     * createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
     * cls, qName));
     * cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
     * .
     * createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class
     * , cls, qName));
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "ReferenceType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2001._04.xmlenc.ReferenceType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#",
     * "TransformsType"); cachedSerQNames.add(qName); cls =
     * org.w3.www._2000._09.xmldsig.TransformType[].class;
     * cachedSerClasses.add(cls); qName = new
     * javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#",
     * "Transform"); qName2 = null; cachedSerFactories.add(new
     * org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
     * cachedDeserFactories.add(new
     * org.apache.axis.encoding.ser.ArrayDeserializerFactory());
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "ActionType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.ActionType.class;
     * cachedSerClasses.add(cls);
     * cachedSerFactories.add(org.apache.axis.encoding
     * .ser.BaseSerializerFactory.
     * createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
     * cls, qName));
     * cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
     * .
     * createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class
     * , cls, qName));
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "AdviceType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.AdviceType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "AssertionType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.AssertionType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "AttributeStatementType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.AttributeStatementType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "AttributeType"); cachedSerQNames.add(qName); cls =
     * java.lang.Object[].class; cachedSerClasses.add(cls); qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "AttributeValue"); qName2 = null; cachedSerFactories.add(new
     * org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
     * cachedDeserFactories.add(new
     * org.apache.axis.encoding.ser.ArrayDeserializerFactory());
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "AudienceRestrictionType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.AudienceRestrictionType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "AuthnContextType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.AuthnContextType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "AuthnStatementType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.AuthnStatementType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "AuthzDecisionStatementType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.AuthzDecisionStatementType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "BaseIDAbstractType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.BaseIDAbstractType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "ConditionAbstractType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.ConditionAbstractType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "ConditionsType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.ConditionsType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "DecisionType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.DecisionType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(enumsf);
     * cachedDeserFactories.add(enumdf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "EncryptedElementType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.EncryptedElementType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "EvidenceType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.EvidenceType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "KeyInfoConfirmationDataType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.KeyInfoConfirmationDataType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "NameIDType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.NameIDType.class;
     * cachedSerClasses.add(cls);
     * cachedSerFactories.add(org.apache.axis.encoding
     * .ser.BaseSerializerFactory.
     * createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class,
     * cls, qName));
     * cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory
     * .
     * createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class
     * , cls, qName));
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "OneTimeUseType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.OneTimeUseType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "ProxyRestrictionType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.ProxyRestrictionType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "StatementAbstractType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.StatementAbstractType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "SubjectConfirmationDataType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.SubjectConfirmationDataType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "SubjectConfirmationType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.SubjectConfirmationType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "SubjectLocalityType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.SubjectLocalityType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:assertion",
     * "SubjectType"); cachedSerQNames.add(qName); cls =
     * assertion._0._2.SAML.tc.names.oasis.SubjectType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "ArtifactResolveType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.ArtifactResolveType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "ArtifactResponseType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.ArtifactResponseType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "AssertionIDRequestType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.AssertionIDRequestType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "AttributeQueryType"); cachedSerQNames.add(qName); cls =
     * org.opensaml.saml2.core.AttributeQuery.class; cachedSerClasses.add(cls);
     * cachedSerFactories.add(beansf); cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "AuthnContextComparisonType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.AuthnContextComparisonType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(enumsf);
     * cachedDeserFactories.add(enumdf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "AuthnQueryType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.AuthnQueryType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "AuthnRequestType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.AuthnRequestType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "AuthzDecisionQueryType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.AuthzDecisionQueryType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "ExtensionsType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.ExtensionsType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "IDPEntryType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.IDPEntryType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "IDPListType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.IDPListType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "LogoutRequestType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.LogoutRequestType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "ManageNameIDRequestType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.ManageNameIDRequestType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "NameIDMappingRequestType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.NameIDMappingRequestType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "NameIDMappingResponseType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.NameIDMappingResponseType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "NameIDPolicyType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.NameIDPolicyType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "RequestAbstractType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.RequestAbstractType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "RequestedAuthnContextType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.RequestedAuthnContextType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "ResponseType"); cachedSerQNames.add(qName); cls =
     * org.opensaml.saml2.core.Response.class; cachedSerClasses.add(cls);
     * cachedSerFactories.add(beansf); cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "ScopingType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.ScopingType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "StatusCodeType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.StatusCodeType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "StatusDetailType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.StatusDetailType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "StatusResponseType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.StatusResponseType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "StatusType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.StatusType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "SubjectQueryAbstractType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.SubjectQueryAbstractType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     * 
     * qName = new
     * javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol",
     * "TerminateType"); cachedSerQNames.add(qName); cls =
     * protocol._0._2.SAML.tc.names.oasis.TerminateType.class;
     * cachedSerClasses.add(cls); cachedSerFactories.add(beansf);
     * cachedDeserFactories.add(beandf);
     */
  }

  protected org.apache.axis.client.Call createCall()
    throws java.rmi.RemoteException {

    try {
      org.apache.axis.client.Call _call = super._createCall();
      if (super.maintainSessionSet) {
        _call.setMaintainSession(super.maintainSession);
      }
      if (super.cachedUsername != null) {
        _call.setUsername(super.cachedUsername);
      }
      if (super.cachedPassword != null) {
        _call.setPassword(super.cachedPassword);
      }
      if (super.cachedEndpoint != null) {
        _call.setTargetEndpointAddress(super.cachedEndpoint);
      }
      if (super.cachedTimeout != null) {
        _call.setTimeout(super.cachedTimeout);
      }
      if (super.cachedPortName != null) {
        _call.setPortName(super.cachedPortName);
      }
      java.util.Enumeration keys = super.cachedProperties.keys();
      while (keys.hasMoreElements()) {
        java.lang.String key = (java.lang.String) keys.nextElement();
        _call.setProperty(key, super.cachedProperties.get(key));
      }
      // All the type mapping information is registered
      // when the first call is made.
      // The type mapping information is actually registered in
      // the TypeMappingRegistry of the service, which
      // is the reason why registration is only needed for the first call.
      synchronized (this) {
        if (firstCall()) {
          // must set encoding style before registering serializers
          _call.setEncodingStyle(null);
          for (int i = 0; i < cachedSerFactories.size(); ++i) {
            java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
            javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames
              .get(i);
            java.lang.Object x = cachedSerFactories.get(i);
            if (x instanceof Class) {
              java.lang.Class sf = (java.lang.Class) cachedSerFactories.get(i);
              java.lang.Class df = (java.lang.Class) cachedDeserFactories
                .get(i);
              _call.registerTypeMapping(cls, qName, sf, df, false);
            } else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
              org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories
                .get(i);
              org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories
                .get(i);
              _call.registerTypeMapping(cls, qName, sf, df, false);
            }
          }
        }
      }
      return _call;
    } catch (java.lang.Throwable _t) {
      throw new org.apache.axis.AxisFault(
        "Failure trying to get the Call object", _t);
    }
  }

  public org.opensaml.saml2.core.Response attributeQuery(
    org.opensaml.saml2.core.AttributeQuery body)
    throws java.rmi.RemoteException {

    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[0]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setEncodingStyle(null);
    _call
      .setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
    _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS,
      Boolean.FALSE);
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName("", "method"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { body });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (org.opensaml.saml2.core.Response) _resp;
        } catch (java.lang.Exception _exception) {
          return (org.opensaml.saml2.core.Response) org.apache.axis.utils.JavaUtils
            .convert(_resp, org.opensaml.saml2.core.Response.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

}
