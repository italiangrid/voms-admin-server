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
	@Column(nullable=false, unique=true, name="ISO_CODE", length=2)
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
