package org.glite.security.voms.admin.model.request;

import java.io.Serializable;
import java.util.Date;



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
    
    Long id;
    
    Date creationDate;
    Date expiryDate;
    Date completionDate;
    
    RequesterInfo requesterInfo;
    
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
     * @return the requesterInfo
     */
    public RequesterInfo getRequesterInfo() {
    
        return requesterInfo;
    }

    
    /**
     * @param requesterInfo the requesterInfo to set
     */
    public void setRequesterInfo( RequesterInfo requesterInfo ) {
    
        this.requesterInfo = requesterInfo;
    }

    
    /**
     * @return the status
     */
    public StatusFlag getStatus() {
    
        return status;
    }

    
    /**
     * @param status the status to set
     */
    public void setStatus( StatusFlag status ) {
    
        this.status = status;
    }
    
}
