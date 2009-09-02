package org.glite.security.voms.admin.model.request;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "role_membership_req")
public class RoleMembershipRequest extends Request {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	String groupName;
	String roleName;

	public RoleMembershipRequest() {
	}
	
	

	public String getGroupName() {
		return groupName;
	}



	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}



	/**
	 * @return the roleName
	 */
	public String getRoleName() {

		return roleName;
	}

	/**
	 * @param roleName
	 *            the roleName to set
	 */
	public void setRoleName(String roleName) {

		this.roleName = roleName;
	}
	
	public String getTypeName() {

		return "Role membership request";
	}
	
	public String getFQAN(){
		return getGroupName()+"/Role="+getRoleName();
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		
		if (!(other instanceof RoleMembershipRequest))
			return false;
		
		if (other == null)
			return false;
		
		RoleMembershipRequest that = (RoleMembershipRequest)other;
		EqualsBuilder builder = new EqualsBuilder();
		
		return builder.appendSuper(super.equals(other)).append(groupName, that.groupName).append(roleName, that.roleName).isEquals();
	}
	
	@Override
	public int hashCode() {
		
		HashCodeBuilder builder = new HashCodeBuilder(17,43);
		builder.appendSuper(super.hashCode()).append(groupName).append(roleName);
		return builder.toHashCode();
	}
	
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.appendSuper(super.toString()).append("groupName", groupName).append("roleName", roleName);
		return builder.toString();
	}
}
