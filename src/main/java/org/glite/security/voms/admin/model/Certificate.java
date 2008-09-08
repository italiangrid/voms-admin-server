package org.glite.security.voms.admin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.digester.substitution.MultiVariableExpander;



public class Certificate implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected Long id;

    protected String subjectString;

    protected X500Principal subjectDER;

    protected VOMSCA ca;

    protected String email;

    protected boolean suspended;
    
    protected String suspensionReason;

    protected Date notAfter;

    protected Date creationTime;

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

    public X500Principal getSubjectDER() {

        return subjectDER;
    }

    public void setSubjectDER( X500Principal subjectDER ) {

        this.subjectDER = subjectDER;
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

    
}
