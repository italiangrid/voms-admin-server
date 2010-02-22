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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.error.VOMSException;
import org.opensaml.xml.util.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;



public class VOMSResponseFactory {
    
    private static final Log log = LogFactory
            .getLog( VOMSResponseFactory.class );
    
    private static VOMSResponseFactory instance = null;
    
    protected DocumentBuilder docBuilder;
    
    
    private VOMSResponseFactory() {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments( true );
        factory.setNamespaceAware( false );
        factory.setValidating( false );

        try {
            docBuilder = factory.newDocumentBuilder();
        } catch ( ParserConfigurationException e ) {
            
            log.error( "Error configuring DOM document builder." );
            
            if (log.isDebugEnabled()){
                log.error( e.getMessage(), e );
            }
            
            throw new VOMSException(e);
        }

    }
    
    public static VOMSResponseFactory instance() {
        
        if (instance == null)
            instance = new VOMSResponseFactory();

        return instance;
    }
    
    public Document buildResponse(byte[] acBytes){
        
        assert acBytes != null && acBytes.length != 0 : "Cannot build a response for a null AC!";
        
        Document response = docBuilder.newDocument();
        VOMSResponseFragment frag = new VOMSResponseFragment(response);
        
        frag.buildACElement( Base64.encodeBytes( acBytes ) );
        response.appendChild( frag.getFragment() );
        
        return response;
    }
    
    public Document buildErrorResponse(String vomsErrorCode, String errorMessage){
        
        assert errorMessage != null: "Cannot build a response for a null errorMessage!";
        Document response = docBuilder.newDocument();
        VOMSResponseFragment frag = new VOMSResponseFragment(response);
        
        frag.buildErrorElement( vomsErrorCode, errorMessage );
        response.appendChild( frag.getFragment() );
        
        return response;
        
    }
}


class VOMSResponseFragment{
    
    private Document doc;
    DocumentFragment fragment;
    
    
    public VOMSResponseFragment(Document document) {

        this.doc = document;
        fragment = doc.createDocumentFragment();
      
    }
    
    public void buildACElement(String base64EncodedACString){
    
    	Element root = doc.createElement( "voms" );
    	fragment.appendChild( root );
    	
        Element ac = doc.createElement( "ac" );
        appendTextChild( ac, base64EncodedACString );
        root.appendChild( ac );
        
    }
    
    
    public void buildErrorElement(String errorCode, String errorMessage){
    	 
        Element error = doc.createElement( "error" );
        Element errorCodeElement = doc.createElement("code");
        Element errorMessageElement = doc.createElement("message");
        
        appendTextChild( errorMessageElement, errorMessage);
        appendTextChild(errorCodeElement, errorCode);
        error.appendChild(errorCodeElement);
        error.appendChild(errorMessageElement);
        
        fragment.appendChild(error);
    }
    
    public DocumentFragment getFragment() {

        return fragment;
    }
    
    protected void appendTextChild(Element e, String text){
        assert e != null : "Cannot append a text child to a null element!";
        assert text != null: "Cannot append a null text to an element!";
        
        e.appendChild( doc.createTextNode( text ) );
        
    }
    
}
