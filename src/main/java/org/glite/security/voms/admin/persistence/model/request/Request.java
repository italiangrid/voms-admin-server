/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.persistence.model.request;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.glite.security.voms.admin.persistence.model.NamedType;

@Entity
@Table(name = "req")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Request implements Serializable, NamedType {

	public enum STATUS {
		SUBMITTED, CONFIRMED, PENDING, APPROVED, REJECTED
	}

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "request_id")
	Long id;

	Date creationDate;
	Date expirationDate;
	Date completionDate;

	@OneToOne(optional = false, cascade = { CascadeType.ALL })
	@JoinColumn(name = "requester_info_id", nullable = false, updatable = false)
	RequesterInfo requesterInfo;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	STATUS status;

	/**
	 * @return the id
	 */
	public Long getId() {

		return id;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {

		return creationDate;
	}

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {

		return expirationDate;
	}

	/**
	 * @return the completionDate
	 */
	public Date getCompletionDate() {

		return completionDate;
	}

	/**
	 * @return the requesterInfo
	 */
	public RequesterInfo getRequesterInfo() {

		return requesterInfo;
	}

	/**
	 * @return the status
	 */
	public STATUS getStatus() {

		return status;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {

		this.creationDate = creationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {

		this.expirationDate = expirationDate;
	}

	/**
	 * @param completionDate
	 *            the completionDate to set
	 */
	public void setCompletionDate(Date completionDate) {

		this.completionDate = completionDate;
	}

	/**
	 * @param requesterInfo
	 *            the requesterInfo to set
	 */
	public void setRequesterInfo(RequesterInfo requesterInfo) {

		this.requesterInfo = requesterInfo;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(STATUS status) {

		this.status = status;
	}

	@Override
	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof Request))
			return false;

		if (other == null)
			return false;

		final Request that = (Request) other;

		EqualsBuilder builder = new EqualsBuilder();

		builder.append(creationDate, that.creationDate).append(requesterInfo,
				that.requesterInfo).append(status, that.status).append(
				completionDate, that.completionDate).append(expirationDate,
				that.expirationDate);

		return builder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(17, 37);

		builder.append(creationDate).append(requesterInfo).append(status)
				.append(completionDate).append(expirationDate);
		return builder.toHashCode();
	}

	public void approve() {

		setStatus(STATUS.APPROVED);
		setCompletionDate(new Date());
	}

	public void reject() {

		setStatus(STATUS.REJECTED);
		setCompletionDate(new Date());

	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		
		builder.append("id",id).
			append("status", status).
			append("requesterInfo", requesterInfo).
			append("creationDate",creationDate).append("expirationDate",expirationDate).append("completionDate",completionDate);
		
		return builder.toString();
	}
}
