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

package org.glite.security.voms.admin.integration.orgdb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="EXPERIMENTS")
public class Experiment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length=60, name="NAME")
	String name;
	
	@Column(length=300,name="TITLE")
	String title;
	
	@Column(nullable=false, length=2, name="STATUS")
	String status;
	
	@ManyToOne(optional=true)
	@JoinColumn(name="PARENT_EXPERIMENT")
	Experiment parent;
	
	@Column(name="BEAM", length=20)
	String beam;
	
	@Column(name="HOME_PAGE", length=150)
	String homePage;
	
	@Column(name="DOCUMENTATION_PAGE", length=150)
	String documentationPage;
	
	@Column(name="PROGRAMME", length=10)
	String programme;
	
	@Column(name="MNEMONIC", length=20)
	String mnemonic;
	
	@Column(name="GB_FLAG", length=1, nullable=false)
	String gbFlag;
	
	@Column(name="EMAIL", length=150)
	String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Experiment getParent() {
		return parent;
	}

	public void setParent(Experiment parent) {
		this.parent = parent;
	}

	public String getBeam() {
		return beam;
	}

	public void setBeam(String beam) {
		this.beam = beam;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getDocumentationPage() {
		return documentationPage;
	}

	public void setDocumentationPage(String documentationPage) {
		this.documentationPage = documentationPage;
	}

	public String getProgramme() {
		return programme;
	}

	public void setProgramme(String programme) {
		this.programme = programme;
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}

	public String getGbFlag() {
		return gbFlag;
	}

	public void setGbFlag(String gbFlag) {
		this.gbFlag = gbFlag;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Experiment other = (Experiment) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Experiment [name=").append(name).append(", status=")
				.append(status).append(", mnemonic=").append(mnemonic).append(
						", email=").append(email).append("]");
		return builder.toString();
	}

	
	
}
