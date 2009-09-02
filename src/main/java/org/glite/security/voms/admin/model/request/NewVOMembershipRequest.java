package org.glite.security.voms.admin.model.request;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "vo_membership_req")
public class NewVOMembershipRequest extends Request {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	String confirmId;

	public NewVOMembershipRequest() {

		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the confirmId
	 */
	public String getConfirmId() {

		return confirmId;
	}

	/**
	 * @param confirmId
	 *            the confirmId to set
	 */
	public void setConfirmId(String confirmId) {

		this.confirmId = confirmId;
	}

	public String getTypeName() {

		return "VO membership request";
	}
	
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		
		builder.appendSuper(super.toString()).append("confirmId", confirmId);
		return builder.toString();
	}

}
