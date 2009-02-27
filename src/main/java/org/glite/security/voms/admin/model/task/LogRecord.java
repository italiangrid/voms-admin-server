package org.glite.security.voms.admin.model.task;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.task.Task.TaskStatus;

@Entity
@Table(name="task_log_record")
public class LogRecord implements Serializable, Comparable <LogRecord>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue
    Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    TaskStatus event;
    
    @Column(nullable=false)
    Date date;
    
    @ManyToOne
    @JoinColumn(name="task_id", nullable=false)
    Task task;
    
    @ManyToOne
    @JoinColumn(name="admin_id")
    VOMSAdmin admin;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    VOMSUser user;
    
    
    public LogRecord() {

        // TODO Auto-generated constructor stub
    }


    
    /**
     * @return the id
     */
    public Long getId() {
    
        return id;
    }


    
    /**
     * @return the event
     */
    public TaskStatus getEvent() {
    
        return event;
    }


    
    /**
     * @return the date
     */
    public Date getDate() {
    
        return date;
    }


    
    /**
     * @return the admin
     */
    public VOMSAdmin getAdmin() {
    
        return admin;
    }


    
    /**
     * @return the user
     */
    public VOMSUser getUser() {
    
        return user;
    }


    
    /**
     * @param id the id to set
     */
    public void setId( Long id ) {
    
        this.id = id;
    }


    
    /**
     * @param event the event to set
     */
    public void setEvent( TaskStatus event ) {
    
        this.event = event;
    }


    
    /**
     * @param date the date to set
     */
    public void setDate( Date date ) {
    
        this.date = date;
    }


    
    /**
     * @param admin the admin to set
     */
    public void setAdmin( VOMSAdmin admin ) {
    
        this.admin = admin;
    }


    
    /**
     * @param user the user to set
     */
    public void setUser( VOMSUser user ) {
    
        this.user = user;
    }

    
    

    public int compareTo( LogRecord o ) {

        if (o == null) 
            return 1;
        
        return - o.getDate().compareTo( o.getDate() );
    }



    
    /**
     * @return the task
     */
    public Task getTask() {
    
        return task;
    }



    
    /**
     * @param task the task to set
     */
    public void setTask( Task task ) {
    
        this.task = task;
    }
    
    
    public boolean equals( Object other ) {

        if ( this == other )
            return true;
        if ( !( other instanceof LogRecord ) )
            return false;

        if ( other == null )
            return false;

        LogRecord that = (LogRecord) other;
        
        if (!getTask().equals( that.getTask() ))
            return false;
        if (!getDate().equals( that.getDate() ))
            return false;
        if (!getEvent().equals( that.getEvent() ))
            return false;
        if (getUser() != null && that.getUser()!= null)
            return getUser().equals( that.getUser() );
        if (getAdmin()!= null && that.getAdmin()!= null)
            return getAdmin().equals( that.getAdmin() );
        
        return true;
        
    }
    
    @Override
    public int hashCode() {
        
        return new HashCodeBuilder(17,35).append( task ).append( date ).append(event).append( user ).append(admin).toHashCode();
        
    }
}
