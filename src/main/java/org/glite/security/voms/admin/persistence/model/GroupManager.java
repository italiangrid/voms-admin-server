package org.glite.security.voms.admin.persistence.model;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "managers")
public class GroupManager {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "VOMS_GRPMAN_SEQ")
	@SequenceGenerator(name = "VOMS_GRPMAN_SEQ", sequenceName = "VOMS_GRPMAN_SEQ")
	Long id;

	@Column(name = "name", nullable = false, unique=true)
	String name;
	
	@Column(nullable=false)
	String description;

	@Column(name="email_address", nullable=false)
	String emailAddress;
	
	@ManyToMany
	@JoinTable(
		name="managers_groups",
		joinColumns={@JoinColumn(name="manager_id", referencedColumnName="id")},
		inverseJoinColumns={@JoinColumn(name="group_id", referencedColumnName="gid")})
	Set<VOMSGroup> managedGroups = new TreeSet<VOMSGroup>();

	public GroupManager() {

	}

	/**
	 * @return the id
	 */
	public Long getId() {

		return id;
	}

	/**
	 * @param id
	 *          the id to set
	 */
	public void setId(Long id) {

		this.id = id;
	}


	/**
	 * @return the managedGroups
	 */
	public Set<VOMSGroup> getManagedGroups() {

		return managedGroups;
	}

	/**
	 * @param managedGroups
	 *          the managedGroups to set
	 */
	public void setManagedGroups(Set<VOMSGroup> managedGroups) {

		this.managedGroups = managedGroups;
	}

	
	/**
	 * @return the name
	 */
	public String getName() {
	
		return name;
	}

	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
	
		this.name = name;
	}

	
	/**
	 * @return the description
	 */
	public String getDescription() {
	
		return description;
	}

	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
	
		this.description = description;
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
		GroupManager other = (GroupManager) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
	
		return emailAddress;
	}

	
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
	
		this.emailAddress = emailAddress;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return "GroupManager [id=" + id + ", name=" + name + ", emailAddress="
			+ emailAddress + ", managedGroups=" + managedGroups + "]";
	}
	
	
}
