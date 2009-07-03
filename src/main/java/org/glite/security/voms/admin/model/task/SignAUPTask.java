package org.glite.security.voms.admin.model.task;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.glite.security.voms.admin.model.AUP;


@Entity
@Table(name="sign_aup_task")
public class SignAUPTask extends Task implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    @ManyToOne
    @JoinColumn(name="aup_id", updatable=false, nullable=false)
    AUP aup;

    
    public SignAUPTask() {

        // TODO Auto-generated constructor stub
    }

   
    public SignAUPTask(TaskType tt, AUP a, Date expiryDate){
        type = tt;
        aup = a;
        this.expiryDate = expiryDate;
        creationDate = new Date(  );
        status = TaskStatus.CREATED;
       
        addLogRecord(getCreationDate());
        
        
    }
    /**
     * @return the aup
     */
    public AUP getAup() {
    
        return aup;
    }
    
    /**
     * @param aup the aup to set
     */
    public void setAup( AUP aup ) {
    
        this.aup = aup;
    }
    
 
    @Override
    public String toString() {
    
        return String.format( "SignAUPTask[id:%d, type:%s, aup:%s, user:%s, expires:%s]", getId(),getType(),getAup(), getUser().toString(), getExpiryDate());
        
    }
}
