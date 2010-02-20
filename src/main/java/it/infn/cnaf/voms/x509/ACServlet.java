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

package it.infn.cnaf.voms.x509;

import it.infn.cnaf.voms.aa.VOMSAA;
import it.infn.cnaf.voms.aa.VOMSAttributeAuthority;
import it.infn.cnaf.voms.aa.VOMSAttributes;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.x509.X509V2AttributeCertificate;
import org.glite.security.SecurityContext;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.error.VOMSSyntaxException;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.error.NoSuchCertificateException;
import org.glite.security.voms.admin.persistence.error.NoSuchUserException;
import org.glite.security.voms.admin.persistence.error.SuspendedCertificateException;
import org.glite.security.voms.admin.persistence.error.SuspendedUserException;
import org.w3c.dom.Document;


public class ACServlet extends BaseServlet implements VOMSErrorCodes {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private static final Log log = LogFactory.getLog( ACServlet.class );
    private TransformerFactory transformerFactory = TransformerFactory.newInstance();
    
    
    protected void serializeXMLDoc(Document doc, Writer writer) {
        
        Transformer transformer;
        
        try {
        
            transformer = transformerFactory.newTransformer();
        
        } catch ( TransformerConfigurationException e ) {
            
            log.error("Error creating XML transformer:"+e.getMessage());
            if (log.isDebugEnabled())
                log.error( e.getMessage(),e );
            
            throw new VOMSException("Error creating XML transformer:", e);
            
        }
        
        DOMSource source = new DOMSource( doc );
        StreamResult res = new StreamResult(writer);
        
        try {
            
            transformer.transform( source, res );
            writer.flush();
        
        } catch ( Exception e ) {
            
            log.error("Error caught serializing XML :"+e.getMessage());
            if (log.isDebugEnabled())
                log.error( e.getMessage(),e );
            
            throw new VOMSException("Error caugh serializing XML :", e);
        
        }
    }
    
    protected String xmlDocAsString(Document doc){ 
        
        Transformer transformer;
        
        try {
        
            transformer = transformerFactory.newTransformer();
        
        } catch ( TransformerConfigurationException e ) {
            
            log.error("Error creating XML transformer:"+e.getMessage());
            if (log.isDebugEnabled())
                log.error( e.getMessage(),e );
            
            throw new VOMSException("Error creating XML transformer:", e);
            
        }
        StringWriter writer = new StringWriter();
        
        DOMSource source = new DOMSource( doc );
        StreamResult res = new StreamResult(writer);
        
        try {
            
            transformer.transform( source, res );
        
        } catch ( TransformerException e ) {
            
            log.error("Error caught serializing XML :"+e.getMessage());
            if (log.isDebugEnabled())
                log.error( e.getMessage(),e );
            
            throw new VOMSException("Error caugh serializing XML :", e);
        
        }
        writer.flush();
        
        return writer.toString();
    }

    protected void writeErrorResponse(HttpServletResponse response, int httpErrorCode, String vomsErrorCode, String message)
        throws ServletException, IOException{
        
        VOMSResponseFactory responseFactory = VOMSResponseFactory.instance();
        
        
        Document xmlResponse = responseFactory.buildErrorResponse( vomsErrorCode, message);
        
        response.setContentType( "text/xml" );
        response.setCharacterEncoding( "UTF-8" );
        response.setStatus( httpErrorCode );
        
        serializeXMLDoc( xmlResponse, response.getWriter());
        return;
    }
    
    protected void writeResponse(HttpServletResponse response, byte[] ac)    
        throws ServletException, IOException{
        
        VOMSResponseFactory responseFactory = VOMSResponseFactory.instance();
        Document xmlResponse = responseFactory.buildResponse( ac );
        
        response.setContentType( "text/xml" );
        response.setCharacterEncoding( "UTF-8" );
        
        response.getWriter().write( xmlDocAsString( xmlResponse ) );
        return;
        
    }
    
    
    protected List<String> parseRequestedFQANs(HttpServletRequest request){
    	
    	String fqansString = request.getParameter("fqans");
    	
    	if (fqansString == null)
    		return null;
    	
    	List<String> requestedFQANs = new ArrayList<String>();
    	
    	
    	if (fqansString.contains(",")){
    		
    		for (String s: StringUtils.split(fqansString,","))
    			requestedFQANs.add(s);
    	}else
    		requestedFQANs.add(fqansString);
    	
    	return requestedFQANs;
    	
    }
    
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException , IOException {
    
        VOMSAttributeAuthority vomsAA = VOMSAA.getVOMSAttributeAuthority();
        VOMSAttributes attrs;
        
        if (!VOMSConfiguration.instance().getBoolean(VOMSConfigurationConstants.VOMS_AA_REST_ACTIVATE_ENDPOINT, false)){
        	writeErrorResponse(response, 500, 
        			VOMS_ERROR_INTERNAL_ERROR, 
        			"REST endpoint is disabled for this VO");
        	return;
        }
        
        if (CurrentAdmin.instance().isUnauthenticated()){
        	writeErrorResponse(response, 400, 
        			VOMS_ERROR_BAD_REQUEST, 
        			"Please authenticated with an X509 certificate to obtain a VOMS attribute certificate");
        	return;
        }
        
        String clientDN = CurrentAdmin.instance().getRealSubject();
        
        List<String> requestedFQANs = parseRequestedFQANs(request);
        
        try{
            
            if (requestedFQANs == null || requestedFQANs.size() == 0)
                attrs = vomsAA.getVOMSAttributes(clientDN);
            else
                attrs = vomsAA.getVOMSAttributes( clientDN, requestedFQANs );
        
        }catch(VOMSException e){
            
        	int httpErrorCode;
            String vomsErrorCode;
            
            log.error("Error getting VOMS attributes for user '"+clientDN+"':"+e.getMessage());
            
            
            if (e instanceof NoSuchUserException || e instanceof NoSuchCertificateException){            
            	httpErrorCode = 403;
                vomsErrorCode = VOMS_ERROR_NO_SUCH_USER;
            }
            else if (e instanceof SuspendedUserException){
            	httpErrorCode = 403;
            	vomsErrorCode = VOMS_ERROR_SUSPENDED_USER;
            }
            else if (e instanceof SuspendedCertificateException){
            	httpErrorCode = 403;
            	vomsErrorCode = VOMS_ERROR_SUSPENDED_CERTIFICATE;
            	
            }else if (e instanceof VOMSSyntaxException){
            	httpErrorCode = 400;
            	vomsErrorCode = VOMS_ERROR_BAD_REQUEST;
            }
            else{
            	httpErrorCode = 500;
            	vomsErrorCode = VOMS_ERROR_INTERNAL_ERROR;
            }
            writeErrorResponse( response, httpErrorCode,vomsErrorCode, e.getMessage() );
            return;
        }
        
        long lifetime = -1;
        
                
        try{
            String lifetimeString = request.getParameter( "lifetime" );
        
            if (lifetimeString != null)
                lifetime = parseLong( request.getParameter( "lifetime" ));
        
        }catch (NumberFormatException e){
            
            // Ignore rubbish in lifetime parameter
        }
        
        SecurityContext ctxt = SecurityContext.getCurrentContext();
        
        // Handle unauthenticated clients here
        if (ctxt.getClientCert() == null)
        	throw new VOMSException("No client certificate found in the request!");
        
        X509ACGenerator acGen = X509ACGenerator.instance();
        
        if (lifetime > 0)
            acGen.setLifetime( lifetime );
        
        X509V2AttributeCertificate ac = acGen.generateVOMSAttributeCertificate( ctxt.getClientCert(), attrs );
        
        byte[] acBytes = null;
        
        try {
            
            acBytes =  ac.getEncoded();                
                    
        } catch ( IOException e ) {
            log.error("Error encoding user attribute certificate: "+e.getMessage());
            writeErrorResponse( response, 500, VOMS_ERROR_INTERNAL_ERROR, e.getMessage() );
            return;
        }
        
        // Add FQANs header to response (useful for debugging purposes)
        response.addHeader("VomsFQANs", StringUtils.join(attrs.getFqans(),","));
        response.addHeader("VomsGenericAttributes", StringUtils.join(attrs.getGenericAttributes(),","));
        
        writeResponse( response, acBytes );
        return;
    }
}
