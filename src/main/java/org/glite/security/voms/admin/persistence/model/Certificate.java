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
package org.glite.security.voms.admin.persistence.model;

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

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "certificate")
public class Certificate implements Serializable, Comparable<Certificate> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Column(name = "subject_string", nullable = false)
	@NaturalId
	protected String subjectString;

	@ManyToOne(targetEntity = VOMSCA.class, optional = false)
	@JoinColumn(name = "ca_id", nullable = false)
	@NaturalId
	protected VOMSCA ca;

	@Transient
	protected String email;

	@Column(nullable = false)
	protected boolean suspended;

	@Enumerated(EnumType.STRING)
	@Column(name = "suspension_reason_code")
	protected VOMSUser.SuspensionReason suspensionReasonCode;

	// FIXME: temporary change to suspended_reason to make it work
	// with VOMS
	@Column(name = "suspended_reason")
	protected String suspensionReason;

	@Column(nullable = false, name = "creation_time")
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

	public void setCa(VOMSCA ca) {

		this.ca = ca;
	}

	public Date getCreationTime() {

		return creationTime;
	}

	public void setCreationTime(Date creationTime) {

		this.creationTime = creationTime;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public String getSubjectString() {

		return subjectString;
	}

	public void setSubjectString(String subjectString) {

		this.subjectString = subjectString;
	}

	public boolean isSuspended() {

		return suspended;
	}

	public void setSuspended(boolean suspended) {

		this.suspended = suspended;
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;
		
		if (other == null)
			return false;

		if (!(other instanceof Certificate))
			return false;

		Certificate that = (Certificate) other;
		
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(getSubjectString(), that.getSubjectString()).append(getCa(),that.getCa());

		return builder.isEquals();

	}

	public int hashCode() {

		HashCodeBuilder builder = new HashCodeBuilder(7, 37);
		builder.append(getSubjectString()).append(getCa());

		return builder.toHashCode();
	}

	public VOMSUser getUser() {
		return user;
	}

	public void setUser(VOMSUser user) {
		this.user = user;
	}

	@Override
	public String toString() {

		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("subject_string",subjectString).append("ca",ca);
		
		return builder.toString();
		
	}

	private List dnAsList(String dn) {

		String[] fields = dn.split("/");
		List result = new ArrayList();

		for (String field : fields) {

			if (field.trim().length() == 0)
				continue;

			String[] val = field.split("=");

			result.add(val);

		}

		return result;
	}

	public List getSubjectAsList() {

		return dnAsList(getSubjectString());
	}

	public List getIssuerAsList() {

		return dnAsList(getCa().getSubjectString());
	}

	public String getSuspensionReason() {

		return suspensionReason;
	}

	public void setSuspensionReason(String suspensionReason) {

		this.suspensionReason = suspensionReason;
	}

	public VOMSUser.SuspensionReason getSuspensionReasonCode() {
		return suspensionReasonCode;
	}

	public void setSuspensionReasonCode(
			VOMSUser.SuspensionReason suspensionReasonCode) {
		this.suspensionReasonCode = suspensionReasonCode;
	}

	public void suspend(VOMSUser.SuspensionReason reason) {

		setSuspended(true);
		setSuspensionReasonCode(reason);
		setSuspensionReason(reason.getMessage());
	}

	public void restore() {

		if (isSuspended()) {
			setSuspended(false);
			setSuspensionReasonCode(null);
			setSuspensionReason(null);
		}

	}

	public void restore(SuspensionReason reason) {

		if (isSuspended() && getSuspensionReasonCode().equals(reason)) {

			setSuspended(false);
			setSuspensionReasonCode(null);
			setSuspensionReason(null);

		}
	}

	public int compareTo(Certificate o) {

		CompareToBuilder builder = new CompareToBuilder();
		
		builder.append(subjectString, o.subjectString).append(ca.getSubjectString(), o.ca.getSubjectString());

		return builder.toComparison();
	}
}
