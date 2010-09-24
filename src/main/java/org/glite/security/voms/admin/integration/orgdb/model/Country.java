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
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="COUNTRIES")
public class Country implements Serializable{
	@Id
	@Column(name="ISO_CODE", length=2)
	String isoCode;
	
	@Column(nullable=false, unique=true, name="NAME", length=17)
	String name;
	
	@Column(name="PTT_CODE", length=4)
	String pttCode;
	
	@Column(nullable=false,length=1, name="PREFIX_OR_SUFFIX")
	String prefixOrSuffix;
	
	@Column(nullable=false,length=1, name="BEFORE_PLACE_OR_COUNTRY")
	String beforePlaceOrCountry;
	
	public Country() {
		
	}

	public String getIsoCode() {
		return isoCode;
	}

	public String getName() {
		return name;
	}

	public String getPttCode() {
		return pttCode;
	}

	public String getPrefixOrSuffix() {
		return prefixOrSuffix;
	}

	public String getBeforePlaceOrCountry() {
		return beforePlaceOrCountry;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPttCode(String pttCode) {
		this.pttCode = pttCode;
	}

	public void setPrefixOrSuffix(String prefixOrSuffix) {
		this.prefixOrSuffix = prefixOrSuffix;
	}

	public void setBeforePlaceOrCountry(String beforePlaceOrCountry) {
		this.beforePlaceOrCountry = beforePlaceOrCountry;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		Country that = (Country)obj;
		
		return new EqualsBuilder().append(isoCode, that.isoCode).append(name, that.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 13).append(isoCode).append(name).toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Country [isoCode=").append(isoCode).append(", name=")
				.append(name).append("]");
		return builder.toString();
	}
	
	
}
