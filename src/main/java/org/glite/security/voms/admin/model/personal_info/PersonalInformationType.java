package org.glite.security.voms.admin.model.personal_info;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "personal_info_type")
public class PersonalInformationType {

	public enum Type {
		STRING, PHONE_NUMBER, EMAIL_ADDRESS, URL
	}

	@Id
	@GeneratedValue
	Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	Type type;

	String description;

	/**
	 * @return the id
	 */
	public Long getId() {

		return id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {

		return description;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {

		this.description = description;
	}

	/**
	 * @return the type
	 */
	public Type getType() {

		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {

		this.type = type;
	}

}
