package org.glite.security.voms.admin.model.request;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "membership_rem_req")
public class MembershipRemovalRequest extends Request {

	@Column(nullable=false)
	String reason;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getTypeName() {
		
		return "Membership removal request";
	}

	@Override
	public boolean equals(Object other) {
		
		if (this == other)
			return true;
		
		if (! (other instanceof MembershipRemovalRequest))
			return false;
		
		if (other == null)
			return false;
		
		MembershipRemovalRequest that = (MembershipRemovalRequest)other;
		
		EqualsBuilder builder = new EqualsBuilder();
		
		return builder.appendSuper(super.equals(other)).append(reason, that.getReason()).isEquals();
	}

	@Override
	public int hashCode() {
		
		HashCodeBuilder  builder = new HashCodeBuilder(7, 73);
		builder.appendSuper(super.hashCode()).append(reason);
		return builder.toHashCode();
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Override
	public String toString() {
		
		ToStringBuilder builder = new ToStringBuilder(this);
	
		return builder.appendSuper(super.toString()).append("reason",reason).toString();
	}
	
}
