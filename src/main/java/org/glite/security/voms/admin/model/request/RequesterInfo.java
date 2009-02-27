package org.glite.security.voms.admin.model.request;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

@Entity
@Table( name="requester_info" )
public class RequesterInfo implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    Long id;
    
    String certificateSubject;
    String certificateIssuer;
    
    String emailAddress;
    
    @org.hibernate.annotations.CollectionOfElements
    @JoinTable(name="requester_personal_info", 
            joinColumns=@JoinColumn(name="requester_id")
    )
    @org.hibernate.annotations.MapKey(
            columns=@Column(name="pi_key")
    )
    @Column(name="pi_value")            
    Map <String, String> personalInformation;
    
    Boolean voMember;
    
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

    
    
    public boolean isVoMember(){
        
        return voMember;
    }
    
    /**
     * @return the voMember
     */
    public Boolean getVoMember() {
    
        return voMember;
    }

    
    /**
     * @param voMember the voMember to set
     */
    public void setVoMember( Boolean voMember ) {
    
        this.voMember = voMember;
    }

    
    
    
}
