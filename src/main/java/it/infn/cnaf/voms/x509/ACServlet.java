package it.infn.cnaf.voms.x509;

import it.infn.cnaf.voms.aa.VOMSAA;
import it.infn.cnaf.voms.aa.VOMSAttributeAuthority;
import it.infn.cnaf.voms.aa.VOMSAttributes;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

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
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.service.ServiceUtils;
import org.w3c.dom.Document;


public class ACServlet extends BaseServlet {
    
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

    protected void writeErrorResponse(HttpServletResponse response, int httpErrorCode, String message)
        throws ServletException, IOException{
        
        VOMSResponseFactory responseFactory = VOMSResponseFactory.instance();
        Document xmlResponse = responseFactory.buildErrorResponse( message);
        
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
    
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException , IOException {
    
        VOMSAttributeAuthority vomsAA = VOMSAA.getVOMSAttributeAuthority();
        VOMSAttributes attrs;
        
        String clientDN = CurrentAdmin.instance().getRealSubject();
        String[] requestedFQANs = request.getParameterValues( "fqan" );
        
        try{
            
            if (requestedFQANs == null || requestedFQANs.length == 0)
                attrs = vomsAA.getVOMSAttributes(clientDN);
            else
                attrs = vomsAA.getVOMSAttributes( clientDN, ServiceUtils.toStringList( requestedFQANs ));
        
        }catch(VOMSException e){
            int errorCode;
            
            log.error("Error getting VOMS attributes for user '"+clientDN+"':"+e.getMessage());
            
            if (e instanceof NoSuchUserException)
                errorCode = 404;
            else
                errorCode = 500;
            writeErrorResponse( response, errorCode, e.getMessage() );
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
        X509ACGenerator acGen = X509ACGenerator.instance();
        
        if (lifetime > 0)
            acGen.setLifetime( lifetime );
        
        X509V2AttributeCertificate ac = acGen.generateVOMSAttributeCertificate( ctxt.getClientCert(), attrs );
        
        byte[] acBytes = null;
        
        try {
            
            acBytes =  ac.getEncoded();                
                    
        } catch ( IOException e ) {
            log.error("Error encoding user attribute certificate: "+e.getMessage());
            writeErrorResponse( response, 500, e.getMessage() );
            return;
        }
        
        // Add FQANs header to response (useful for debugging purposes)
        response.addHeader("VomsFQANs", StringUtils.join(attrs.getFqans(),","));
        response.addHeader("VomsGenericAttributes", StringUtils.join(attrs.getGenericAttributes(),","));
        
        writeResponse( response, acBytes );
        return;
    }
}
