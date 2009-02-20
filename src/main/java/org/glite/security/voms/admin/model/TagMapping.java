package org.glite.security.voms.admin.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.glite.security.voms.admin.common.NullArgumentException;

public class TagMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Long id;
	
	Tag tag;
	VOMSGroup group;
	VOMSRole role;
	VOMSAdmin admin;
	
	
	public TagMapping() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean equals(Object other) {
		
		if (this == other)
			return true;
		
		if (other == null)
			return false;
		
		if ( !(other instanceof TagMapping))
			return false;
		
		TagMapping that = (TagMapping)other;
		
		if (!getTag().equals(that.getTag()))
			return false;
		
		if (!getGroup().equals(that.getGroup()))
			return false;
		
		if ((getRole()!= null) && (that.getRole() != null))
			return getRole().equals(that.getRole());
		
		return false;
	}
	
	public int hashCode() {

        int result = 14;

        result = 29 * result + getTag().hashCode();

        result = 29 * result + getGroup().hashCode();

        if ( getRole() != null )
            result = 29 * result + getRole().hashCode();

        return result;
    }
	

	public VOMSGroup getGroup() {
		return group;
	}

	public void setGroup(VOMSGroup group) {
		this.group = group;
	}

	public VOMSRole getRole() {
		return role;
	}

	public void setRole(VOMSRole role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	public boolean isGroupMapping(){
	    
	    if ( tag == null )
            throw new NullArgumentException( "tag cannot be null!" );
	    
	    if ( group == null )
            throw new NullArgumentException( "group cannot be null!" );
	    
	    return (role == null);
	}
	
	public boolean isRoleMapping(){
	    
        return !isGroupMapping();
	    
	}

    
    /**
     * @return the admin
     */
    public VOMSAdmin getAdmin() {
    
        return admin;
    }

    
    /**
     * @param admin the admin to set
     */
    public void setAdmin( VOMSAdmin admin ) {
    
        this.admin = admin;
    }

    
	

}
