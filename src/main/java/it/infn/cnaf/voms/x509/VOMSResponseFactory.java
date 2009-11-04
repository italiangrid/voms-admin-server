package it.infn.cnaf.voms.x509;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSException;
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
            
            log.fatal( "Error configuring DOM document builder." );
            
            if (log.isDebugEnabled()){
                log.debug( e.getMessage(), e );
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
    
    public Document buildErrorResponse(String errorMessage){
        
        assert errorMessage != null: "Cannot build a response for a null errorMessage!";
        Document response = docBuilder.newDocument();
        VOMSResponseFragment frag = new VOMSResponseFragment(response);
        
        frag.buildErrorElement( errorMessage );
        response.appendChild( frag.getFragment() );
        
        return response;
        
    }
}


class VOMSResponseFragment{
    
    private Document doc;
    DocumentFragment fragment;
    
    Element root;
    Element ac;
    Element error;
    
    
    public VOMSResponseFragment(Document document) {

        this.doc = document;
        fragment = doc.createDocumentFragment();
        root = doc.createElement( "voms" );
        fragment.appendChild( root );
        
    }
    
    public void buildACElement(String base64EncodedACString){
        
        ac = doc.createElement( "ac" );
        appendTextChild( ac, base64EncodedACString );
        root.appendChild( ac );
        
    }
    
    
    public void buildErrorElement(String errorMessage){
        error = doc.createElement( "error" );
        appendTextChild( error, errorMessage);
        root.appendChild( error );   
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
