package org.glite.security.voms.admin.model.request;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name = "group_membership_req")
public class GroupMembershipRequest extends Request {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String groupName;

	public GroupMembershipRequest() {
		
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {

		return groupName;
	}

	/**
	 * @param groupName
	 *            the groupName to set
	 */
	public void setGroupName(String groupName) {

		this.groupName = groupName;
	}

	public String getTypeName() {

		return "Group membership request";
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof GroupMembershipRequest))
			return false;

		if (other == null)
			return false;
		
		GroupMembershipRequest that = (GroupMembershipRequest)other;
		EqualsBuilder builder = new EqualsBuilder();
		
		return builder.appendSuper(super.equals(other)).append(groupName, that.groupName).isEquals();
	}
	
	@Override
	public int hashCode() {
		
		HashCodeBuilder builder = new HashCodeBuilder(5, 37);
		builder.appendSuper(super.hashCode()).append(groupName);
		return builder.toHashCode();
		
	}
	
	

}
