package org.glite.security.voms.admin.model.personal_info;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "personal_info")
public class PersonalInformationRecord {

	@Id
	@GeneratedValue
	Long id;

	@ManyToOne
	@JoinColumn(name = "personal_info_type_id", nullable = false)
	PersonalInformationType type;

	String value;

	Boolean visible;

	public PersonalInformationRecord() {

		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public Long getId() {

		return id;
	}

	/**
	 * @return the type
	 */
	public PersonalInformationType getType() {

		return type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {

		return value;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(PersonalInformationType type) {

		this.type = type;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {

		this.value = value;
	}

}
