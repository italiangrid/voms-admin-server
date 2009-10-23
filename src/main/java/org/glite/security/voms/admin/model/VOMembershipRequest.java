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
package org.glite.security.voms.admin.model;

import java.util.Date;

public class VOMembershipRequest {

	public static final Integer SUBMITTED = new Integer(0);
	public static final Integer CONFIRMED = new Integer(1);

	public static final Integer APPROVED = new Integer(2);
	public static final Integer REJECTED = new Integer(3);

	Long id;
	Date creationDate;
	Date evaluationDate;

	String dn;
	String ca;
	String cn;
	String emailAddress;

	Integer status;

	String confirmId;

	public VOMembershipRequest() {

	}

	public Date getCreationDate() {

		return creationDate;
	}

	public void setCreationDate(Date creationDate) {

		this.creationDate = creationDate;
	}

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public Integer getStatus() {

		return status;
	}

	public void setStatus(Integer status) {

		this.status = status;
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof VOMembershipRequest))
			return false;

		VOMembershipRequest that = (VOMembershipRequest) other;
		return getId().equals(that.getId());
	}

	public String getCa() {

		return ca;
	}

	public void setCa(String ca) {

		this.ca = ca;
	}

	public String getCn() {

		return cn;
	}

	public void setCn(String cn) {

		this.cn = cn;
	}

	public String getConfirmId() {

		return confirmId;
	}

	public void setConfirmId(String confirmId) {

		this.confirmId = confirmId;
	}

	public String getDn() {

		return dn;
	}

	public void setDn(String dn) {

		this.dn = dn;
	}

	public String getEmailAddress() {

		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {

		this.emailAddress = emailAddress;
	}

	public int hashCode() {
		return getId().hashCode();
	}

	public Date getEvaluationDate() {

		return evaluationDate;
	}

	public void setEvaluationDate(Date evaluationDate) {

		this.evaluationDate = evaluationDate;
	}

}
