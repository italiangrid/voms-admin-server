package org.glite.security.voms.admin.model.request;

import java.security.cert.X509Certificate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="certificate_request")
public class CertificateRequest extends Request {

    
    /**
     * SERIAL VERSION UID.
     */
    private static final long serialVersionUID = 1084175933731149533L;
    
    
    
    /**
     * The subject of the certificate this request is about
     */
    @Column(nullable=false)
    private String certificateSubject;
    
    /**
     * The issuer of the certificate this request is about
     */
    @Column(nullable=false)
    private String certificateIssuer;
    
    
    /**
     * The X509 certificate this request is about
     */
    private X509Certificate certificate;
    
    /**
     * Constructor
     */
    public CertificateRequest() {

        
    }

    /**
     * @return the certificateSubject
     */
    public String getCertificateSubject() {
    
        return certificateSubject;
    }

    
    /**
     * @return the certificateIssuer
     */
    public String getCertificateIssuer() {
    
        return certificateIssuer;
    }

    
    /**
     * @return the certificate
     */
    public X509Certificate getCertificate() {
    
        return certificate;
    }

    
    /**
     * @param certificateSubject the certificateSubject to set
     */
    public void setCertificateSubject( String certificateSubject ) {
    
        this.certificateSubject = certificateSubject;
    }

    
    /**
     * @param certificateIssuer the certificateIssuer to set
     */
    public void setCertificateIssuer( String certificateIssuer ) {
    
        this.certificateIssuer = certificateIssuer;
    }

    
    /**
     * @param certificate the certificate to set
     */
    public void setCertificate( X509Certificate certificate ) {
    
        this.certificate = certificate;
    }

	public String getTypeName() {
		
		return "Certificate request";
	}
    
    

}
