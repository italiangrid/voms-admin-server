package org.glite.security.voms.admin.model;

import java.io.Serializable;

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
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
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

	public VOMSAdmin getAdmin() {
		return admin;
	}

	public void setAdmin(VOMSAdmin admin) {
		this.admin = admin;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	

}
