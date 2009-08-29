package org.glite.security.voms.admin.model.institution;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name = "institution")
public class Institution {

	@Id
	@Column(name = "institution_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	Date creationDate;

	@Column(unique = true)
	String name;

	String description;

	public Institution() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof Institution))
			return false;

		if (other == null)
			return false;

		Institution that = (Institution) other;

		EqualsBuilder builder = new EqualsBuilder();
		builder.append(name, that.name);

		return builder.isEquals();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 35).append(name).toHashCode();
	}

}
