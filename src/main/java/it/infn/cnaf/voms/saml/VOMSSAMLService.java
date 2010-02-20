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

  Authors: 	Valerio Venturi <valerio.venturi@cnaf.infn.it>
  			Andrea Ceccanti <andrea.ceccanti@cnaf.infn.it>
 
 **************************************************************************/

package it.infn.cnaf.voms.saml;

import it.infn.cnaf.voms.aa.VOMSAA;
import it.infn.cnaf.voms.aa.VOMSAttributeAuthority;
import it.infn.cnaf.voms.aa.VOMSAttributes;
import it.infn.cnaf.voms.saml.exceptions.IssuerPeerMismatchException;
import it.infn.cnaf.voms.saml.exceptions.UnauthorizedQueryException;
import it.infn.cnaf.voms.saml.exceptions.UnknownAttributeException;
import it.infn.cnaf.voms.saml.exceptions.UnsupportedQueryException;
import it.infn.cnaf.voms.saml.exceptions.VersionMismatchException;
import it.infn.cnaf.voms.saml.exceptions.X509SubjectWrongNameIDFormatException;
import it.infn.cnaf.voms.saml.exceptions.X509SubjectWrongNameIDValueException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.util.DNUtil;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;

/**
 * @author Valerio Venturi (valerio.venturi@cnaf.infn.it)
 * @author Andrea Ceccanti (andrea.ceccanti@cnaf.infn.it)
 * 
 */
public class VOMSSAMLService {

	public static final String VOMS_SAML_FQAN_URI = "http://voms.forge.cnaf.infn.it/fqan";
	
    static private Log logger = LogFactory.getLog(VOMSSAMLService.class);
    
    private SAMLAssertionFactory sAMLAssertionFactory;

    private SAMLResponseFactory sAMLResponseFactory;

    private int maxAssertionLifetime;

    public VOMSSAMLService( SAMLAssertionFactory sAMLAssertionFactory, 
            SAMLResponseFactory sAMLResponseFactory,
            int maxAssertionLifetime ) {

        this.sAMLAssertionFactory = sAMLAssertionFactory;
        this.sAMLResponseFactory = sAMLResponseFactory;
        this.maxAssertionLifetime = maxAssertionLifetime;
    }

    public Response attributeQuery( AttributeQuery attributeQuery,
                HttpServletRequest httpServletRequest ) throws RemoteException {
    	
        try {
	
            // get the peer security context
            SecurityContextHelper peerSecurityContext = new SecurityContextHelper(
                    httpServletRequest );

            checkAttributeQuery( attributeQuery, peerSecurityContext );

            String userDN = DNUtil.getBCasX500( attributeQuery.getSubject()
                    .getNameID().getValue() );

            List <String> requestedFQANs = getFQANsFromAttributeQuery( attributeQuery );

            VOMSAttributeAuthority vomsAA = VOMSAA.getVOMSAttributeAuthority();

            // The VOMS attributes, i.e., FQANs + Generic Attributes
            VOMSAttributes attributes = null;

            /* call the core service to get the granted attributes */
            if ( requestedFQANs == null )
                attributes = vomsAA.getVOMSAttributes( userDN );
            
            else if ( requestedFQANs.isEmpty() )
                attributes = vomsAA.getAllVOMSAttributes( userDN );
            
            else
                attributes = vomsAA.getVOMSAttributes( userDN, requestedFQANs );

            /* prepare the Assertion */
            Assertion assertion = sAMLAssertionFactory.create(
                            peerSecurityContext.getCertificate(), 
                            attributes,
                            maxAssertionLifetime );

            /* prepare the Response and return it */
            return sAMLResponseFactory.create( attributeQuery.getID(), assertion );

        } catch ( Throwable exception ) {
            logger.error( exception.getMessage() );
            return sAMLResponseFactory.create( attributeQuery.getID(), exception );
        }
    }

    private void checkAttributeQuery( AttributeQuery attributeQuery,
            SecurityContextHelper peerSecurityContext )
            throws VersionMismatchException ,
            X509SubjectWrongNameIDFormatException ,
            X509SubjectWrongNameIDValueException , IssuerPeerMismatchException ,
            UnauthorizedQueryException , UnsupportedQueryException ,
            UnknownAttributeException {

        // check Version attribute for AttributeQuery is 2.0
        if ( attributeQuery.getVersion() != SAMLVersion.VERSION_20 )
            throw new VersionMismatchException();

        String subjectNameIDFormat = attributeQuery.getSubject().getNameID()
                .getFormat();

        // check the Format attribute of NameID in Subject conforme to SAML X509
        // Profile
        if ( !subjectNameIDFormat
                .equals( NameID.X509_SUBJECT ) )
            throw new X509SubjectWrongNameIDFormatException(
                    subjectNameIDFormat );

        String subjectNameIDValue = attributeQuery.getSubject().getNameID()
                .getValue();

        // check the distinguished name in Subject conform to RFC 2253
        if ( !DNUtil.isRFC2253Conformant( subjectNameIDValue ) )
            throw new X509SubjectWrongNameIDValueException( subjectNameIDValue );

        X500Principal subject = new X500Principal( subjectNameIDValue );

        X500Principal issuer = new X500Principal( attributeQuery.getIssuer()
                .getValue() );

        logger.debug( "Received AttributeQuery issued by " + issuer.getName()
                + " for subject " + subjectNameIDValue );

        // chech the peer identity corresponds to the issuer of the
        // AttributeQuery
        if ( !peerSecurityContext.is( issuer ) )
            throw new IssuerPeerMismatchException( issuer.getName(),
                    peerSecurityContext.getX500Principal().getName() );

        // Check the AttributeQuery is authorized
        if ( !peerSecurityContext.is( subject ) )
            throw new UnauthorizedQueryException( issuer.getName(), subject
                    .getName() );

        // Check requested attributes size.
        // Currently we support only requests for FQAN according to the VOMS
        // SAML profile
        if ( attributeQuery.getAttributes().size() > 1 )
            throw new UnsupportedQueryException();

        if (! attributeQuery.getAttributes().isEmpty()){
            
            Attribute attr = attributeQuery.getAttributes().get( 0 );
            
            if ( !attr.getName().equals( VOMS_SAML_FQAN_URI ) )
                throw new UnknownAttributeException( attr.getName() );
        }
        // logger.info( "AttributeQuery authorized" );

    }

    private List <String> getFQANsFromAttributeQuery( AttributeQuery attributeQuery ) {
    
        // Get only first attribute (currenty VOMS SAML supports requests only
        // for FQANs...)
        if (attributeQuery.getAttributes().isEmpty())
            return null;
        
        Attribute samlAttribute = attributeQuery.getAttributes().get( 0 );
        
        List<XMLObject> attributeValues = samlAttribute.getAttributeValues();
        
        List<String> result = new ArrayList <String>();
        
        for (XMLObject o : attributeValues){
            XSString stringContent = (XSString)o;
            result.add( stringContent.getValue());   
        }
        
        return result;
    }
}
