package org.glite.security.voms.admin.model.task;

import java.util.Date;


public abstract class Task {
    
    public enum TaskStatus {
        CREATED,
        REASSIGNED,
        COMPLETED,
        EXPIRED
    }
    
    Long id;
    
    TaskTypeInfo typeInfo;
    
    Date creationDate;
    Date expiryDate;
    Date completionDate;
        
    TaskStatus status;
    
    /**
     * @return the id
     */
    public Long getId() {
    
        return id;
    }

    
    /**
     * @return the typeInfo
     */
    public TaskTypeInfo getTypeInfo() {
    
        return typeInfo;
    }

    
    /**
     * @param id the id to set
     */
    public void setId( Long id ) {
    
        this.id = id;
    }

    
    /**
     * @param typeInfo the typeInfo to set
     */
    public void setTypeInfo( TaskTypeInfo typeInfo ) {
    
        this.typeInfo = typeInfo;
    }


    
    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
    
        return creationDate;
    }


    
    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate( Date creationDate ) {
    
        this.creationDate = creationDate;
    }


    
    /**
     * @return the status
     */
    public TaskStatus getStatus() {
    
        return status;
    }


    
    /**
     * @param status the status to set
     */
    public void setStatus( TaskStatus status ) {
    
        this.status = status;
    }


    
    /**
     * @return the expiryDate
     */
    public Date getExpiryDate() {
    
        return expiryDate;
    }


    
    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate( Date expiryDate ) {
    
        this.expiryDate = expiryDate;
    }


    
    /**
     * @return the completionDate
     */
    public Date getCompletionDate() {
    
        return completionDate;
    }


    
    /**
     * @param completionDate the completionDate to set
     */
    public void setCompletionDate( Date completionDate ) {
    
        this.completionDate = completionDate;
    }
    
    

}
