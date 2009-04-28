package org.glite.security.voms.admin.model.request;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="req")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Request implements Serializable {

    public enum StatusFlag{
        SUBMITTED,
        CONFIRMED,
        PENDING,
        APPROVED,
        REJECTED
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    @Column(name="request_id")
    Long id;
    
    Date creationDate;
    Date expirationDate;
    Date completionDate;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="requester_info_id", nullable=false, updatable=false)
    RequesterInfo requesterInfo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    StatusFlag status;

    
    /**
     * @return the id
     */
    public Long getId() {
    
        return id;
    }

    
    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
    
        return creationDate;
    }

    
    /**
     * @return the expirationDate
     */
    public Date getExpirationDate() {
    
        return expirationDate;
    }

    
    /**
     * @return the completionDate
     */
    public Date getCompletionDate() {
    
        return completionDate;
    }

    
    /**
     * @return the requesterInfo
     */
    public RequesterInfo getRequesterInfo() {
    
        return requesterInfo;
    }

    
    /**
     * @return the status
     */
    public StatusFlag getStatus() {
    
        return status;
    }

    
    /**
     * @param id the id to set
     */
    public void setId( Long id ) {
    
        this.id = id;
    }

    
    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate( Date creationDate ) {
    
        this.creationDate = creationDate;
    }

    
    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate( Date expirationDate ) {
    
        this.expirationDate = expirationDate;
    }

    
    /**
     * @param completionDate the completionDate to set
     */
    public void setCompletionDate( Date completionDate ) {
    
        this.completionDate = completionDate;
    }

    
    /**
     * @param requesterInfo the requesterInfo to set
     */
    public void setRequesterInfo( RequesterInfo requesterInfo ) {
    
        this.requesterInfo = requesterInfo;
    }

    
    /**
     * @param status the status to set
     */
    public void setStatus( StatusFlag status ) {
    
        this.status = status;
    }
    
    
    
}
