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

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.Security;
import java.util.Collections;
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
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.impl.AttributeBuilder;
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
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSStringBuilder;
import org.w3c.dom.Element;

public class VOMSSAMLClient {
    
    static final String MY_DN = "CN=Andrea Ceccanti,L=CNAF,OU=Personal Certificate,O=INFN,C=IT"; 
    static final String MY_OTHER_DN = "emailAddress=andrea.ceccanti@cnaf.infn.it,CN=cecco,OU=Voms-Admin testing,O=Voms-Admin,ST=Test,C=IT";

    static {

        Security.addProvider( new BouncyCastleProvider() );

    }

    
    public static void main( String[] args ) throws MalformedURLException ,
            ServiceException , RemoteException {

        new VOMSSAMLClient( args[0], args[1] );
    }

    public static Element marshall( XMLObject xmlObject )
            throws MarshallingException {

        Element element;

        MarshallerFactory marshallerFactory = Configuration
                .getMarshallerFactory();
        Marshaller marshaller = marshallerFactory.getMarshaller( xmlObject );
        element = marshaller.marshall( xmlObject );

        return element;
    }

    public VOMSSAMLClient( String host, String vo) throws MalformedURLException,
            ServiceException, RemoteException {

        initializeOpenSAML();
                
        String role = "/mysql/Role=CiccioBomba";
        AttributeQuery query = buildAttributeQuery( MY_DN, Collections.singletonList(role));
        

        System.out.println( "Query:" );
        print( query );

        AttributeAuthorityPortType aa = getVOMSSamlService( host, vo );
        
        Response response = aa.attributeQuery( query );

        System.out.println( "Response:" );
        print( response );
        
    }

    AttributeQuery buildAttributeQuery( String userDn, List<String>  fqans) {

        XMLObjectBuilderFactory bf = Configuration.getBuilderFactory();

        AttributeQueryBuilder qb = (AttributeQueryBuilder) bf
                .getBuilder( AttributeQuery.DEFAULT_ELEMENT_NAME );

        AttributeQuery query = qb.buildObject();

        query.setID( UUID.randomUUID().toString() );
        query.setVersion( SAMLVersion.VERSION_20 );

        query.setIssueInstant( new DateTime() );

        IssuerBuilder issuerBuilder = (IssuerBuilder) bf
                .getBuilder( Issuer.DEFAULT_ELEMENT_NAME );

        Issuer issuer = issuerBuilder.buildObject();
        issuer.setValue( userDn );
        issuer.setFormat( NameID.X509_SUBJECT );

        query.setIssuer( issuer );

        SubjectBuilder subjectBuilder = (SubjectBuilder) bf
                .getBuilder( Subject.DEFAULT_ELEMENT_NAME );
        Subject subject = subjectBuilder.buildObject();

        NameIDBuilder nameIdBuilder = (NameIDBuilder) bf
                .getBuilder( NameID.DEFAULT_ELEMENT_NAME );

        NameID requester = nameIdBuilder.buildObject();
        requester.setFormat( NameID.X509_SUBJECT );
        requester.setValue( userDn );

        subject.setNameID( requester );

        query.setSubject( subject );
        
        
        AttributeBuilder ab = (AttributeBuilder) bf.getBuilder( Attribute.DEFAULT_ELEMENT_NAME );
        Attribute fqanAttr = ab.buildObject();
        
        fqanAttr.setName( "http://voms.forge.cnaf.infn.it/fqan" );
        fqanAttr.setNameFormat( Attribute.UNSPECIFIED );
        
        if (fqans != null){
            
            XSStringBuilder attributeValueBuilder = 
                (XSStringBuilder) bf.getBuilder(XSString.TYPE_NAME);
            
            for (String fqan: fqans){
                
                XSString fqanStringObject = attributeValueBuilder.buildObject( AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME );
                fqanStringObject.setValue( fqan );
                fqanAttr.getAttributeValues().add( fqanStringObject );
            }
            
            query.getAttributes().add( fqanAttr );
        }
        

        return query;
    }

    AttributeAuthorityPortType getVOMSSamlService( String host, String vo ) {

        String url = String.format( "https://%s:8443/voms/%s/services/VOMSSaml", host, vo);
        
        try {

            AttributeAuthorityServiceLocator loc = new AttributeAuthorityServiceLocator();
            TypeMapping typeMapping = loc.getTypeMappingRegistry()
                    .getDefaultTypeMapping();

            typeMapping.register( AttributeQuery.class,
                    AttributeQuery.TYPE_NAME, new SerializerFactory(),
                    new DeserializerFactory() );

            typeMapping.register( Response.class, Response.TYPE_NAME,
                    new SerializerFactory(), new DeserializerFactory() );

            AttributeAuthorityPortType aa = loc
                    .getAttributeAuthorityPortType( new URL( url ) );

            return aa;

        } catch ( Throwable t ) {
            printExceptionAndExit( t );
            
        }

        return null;
    }

    void initializeOpenSAML() {

        try {

            DefaultBootstrap.bootstrap();

        } catch ( ConfigurationException e ) {

            printExceptionAndExit( e );
        }
    }

    void parseCredentials( String certFile, String keyFile ) {

    }

    public void print( XMLObject xmlObject ) {

        try {

            Element element = marshall( xmlObject );

            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty( OutputKeys.INDENT, "yes" );
            tr.setOutputProperty( OutputKeys.METHOD, "xml" );
            tr.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount",
                    String.valueOf( 4 ) );
            tr.transform( new DOMSource( element ), new StreamResult(
                    System.out ) );

        } catch ( Throwable t ) {

            printExceptionAndExit( t );

        }

    }

    public void printExceptionAndExit( Throwable t ) {

        t.printStackTrace();
        System.exit( 1 );

    }
}
