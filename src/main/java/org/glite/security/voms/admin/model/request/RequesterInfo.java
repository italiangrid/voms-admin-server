package org.glite.security.voms.admin.model.request;

import java.io.Serializable;
import java.util.Map;


public class RequesterInfo implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Long id;
    
    String certificateSubject;
    String certificateIssuer;
    
    String emailAddress;
    
    Map <String, String> personalInformation;
    
    /**
     * @return the id
     */
    public Long getId() {
    
        return id;
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
     * @param id the id to set
     */
    public void setId( Long id ) {
    
        this.id = id;
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
     * @return the personalInformation
     */
    public Map <String, String> getPersonalInformation() {
    
        return personalInformation;
    }

    
    /**
     * @param personalInformation the personalInformation to set
     */
    public void setPersonalInformation( Map <String, String> personalInformation ) {
    
        this.personalInformation = personalInformation;
    }

    
    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
    
        return emailAddress;
    }

    
    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress( String emailAddress ) {
    
        this.emailAddress = emailAddress;
    }

    
}
