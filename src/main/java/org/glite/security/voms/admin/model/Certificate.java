package org.glite.security.voms.admin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.glite.security.voms.admin.model.VOMSUser.SuspensionReason;


@Entity
@Table(name="certificate")
public class Certificate implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected Long id;

    @Column(name="subject_string", nullable=false, unique=true)
    protected String subjectString;
    
    
    @ManyToOne(targetEntity=VOMSCA.class, optional=false)
    @JoinColumn(name="ca_id", nullable=false)
    protected VOMSCA ca;

    @Transient
    protected String email;

    @Column(nullable=false)
    protected boolean suspended;
    
    @Enumerated(EnumType.STRING)
    @Column(name="suspension_reason_code")
    protected VOMSUser.SuspensionReason suspensionReasonCode;
    
    @Column(name="suspension_reason")
    protected String suspensionReason;

    @Column(name="not_after")
    protected Date notAfter;

    @Column(nullable=false, name="creation_time")
    protected Date creationTime;

    @ManyToOne
	@JoinColumn(name = "usr_id", nullable = false)
    protected VOMSUser user;
    
    public Certificate() {

        // TODO Auto-generated constructor stub
    }

    public VOMSCA getCa() {

        return ca;
    }

    public void setCa( VOMSCA ca ) {

        this.ca = ca;
    }

    public Date getCreationTime() {

        return creationTime;
    }

    public void setCreationTime( Date creationTime ) {

        this.creationTime = creationTime;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail( String email ) {

        this.email = email;
    }

    public Long getId() {

        return id;
    }

    public void setId( Long id ) {

        this.id = id;
    }

    public Date getNotAfter() {

        return notAfter;
    }

    public void setNotAfter( Date notAfter ) {

        this.notAfter = notAfter;
    }


    public String getSubjectString() {

        return subjectString;
    }

    public void setSubjectString( String subjectString ) {

        this.subjectString = subjectString;
    }

    public boolean isSuspended() {

        return suspended;
    }

    public void setSuspended( boolean suspended ) {

        this.suspended = suspended;
    }

    public boolean equals( Object other ) {

        if ( other == null )
            return false;

        if ( !( other instanceof Certificate ) )
            return false;

        Certificate that = (Certificate) other;

        return getSubjectString().equals( that.getSubjectString() );

    }

    public int hashCode() {

        if ( getSubjectString() == null )
            return -1;

        return getSubjectString().hashCode();
    }

	public VOMSUser getUser() {
		return user;
	}

	public void setUser(VOMSUser user) {
		this.user = user;
	}
    
	@Override
	public String toString() {
		
		return "("+id+":"+subjectString+","+getCa().getSubjectString()+")";
	}
	
	private List dnAsList(String dn){
		
		String[] fields = dn.split("/");
		List result = new ArrayList();
		
		for (String field: fields){
			
			
			if (field.trim().length() == 0)
				continue;
			
			String[] val = field.split("=");
			
			result.add(val);
			
		}
		
		return  result;
	}
	
	
	public List getSubjectAsList(){
		
		return dnAsList(getSubjectString());
	}
	
	public List getIssuerAsList(){
		
		return dnAsList(getCa().getSubjectString());
	}

    
    public String getSuspensionReason() {
    
        return suspensionReason;
    }

    
    public void setSuspensionReason( String suspensionReason ) {
    
        this.suspensionReason = suspensionReason;
    }

	public VOMSUser.SuspensionReason getSuspensionReasonCode() {
		return suspensionReasonCode;
	}

	public void setSuspensionReasonCode(
			VOMSUser.SuspensionReason suspensionReasonCode) {
		this.suspensionReasonCode = suspensionReasonCode;
	}
	
	
	public void suspend(VOMSUser.SuspensionReason reason){
		
		setSuspended(true);
		setSuspensionReasonCode(reason);
		setSuspensionReason(reason.getMessage());
	}
	
	public void restore(SuspensionReason reason){
		
		if (isSuspended() && getSuspensionReasonCode().equals(reason)){
			
			setSuspended(false);
			setSuspensionReasonCode(null);
			setSuspensionReason(null);
			
		}
	}
}
