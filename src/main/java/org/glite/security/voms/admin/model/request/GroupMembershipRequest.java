package org.glite.security.voms.admin.model.request;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "group_membership_req")
public class GroupMembershipRequest extends Request {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private String groupName;

	public GroupMembershipRequest() {

		// TODO Auto-generated constructor stub
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

}
