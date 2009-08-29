package org.glite.security.voms.admin.model.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "task_type")
public class TaskType {

	@Id
	@GeneratedValue
	Long id;

	@Column(nullable = false, unique = true)
	String name;

	String description;

	/**
	 * @return the id
	 */

	public Long getId() {

		return id;
	}

	/**
	 * @return the typeName
	 */

	public String getName() {

		return name;
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
	 * @param typeName
	 *            the typeName to set
	 */
	public void setName(String typeName) {

		this.name = typeName;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {

		this.description = description;
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof TaskType))
			return false;

		if (other == null)
			return false;

		TaskType that = (TaskType) other;

		return this.getName().equals(that.getName());

	}

	@Override
	public int hashCode() {

		if (getName() == null)
			return 0;

		return getName().hashCode();

	}

	@Override
	public String toString() {

		return String.format("[id:%d, name:%s]", getId(), getName());

	}
}
