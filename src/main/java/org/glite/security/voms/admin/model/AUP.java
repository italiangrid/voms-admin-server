package org.glite.security.voms.admin.model;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.database.NoSuchAUPVersionException;

public class AUP implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(AUP.class);

	public static final String VO_AUP_NAME = "VO-AUP";
	public static final String GRID_AUP_NAME = "GRID-AUP";

	Long id;
	String name;
	String description;

	/** Reacceptance period in days **/
	Integer reacceptancePeriod;

	SortedSet<AUPVersion> versions = new TreeSet<AUPVersion>();

	public AUP() {

		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof AUP))
			return false;

		AUP that = (AUP) other;

		if (that == null)
			return false;

		return (that.getName().equals(getName()));
	}

	public AUPVersion getActiveVersion() {

		if (versions.isEmpty())
			return null;

		for (AUPVersion v : versions)
			if (v.getActive())
				return v;

		// No active version, this should never happen
		return null;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {

		return description;
	}

	// Getters and setters
	/**
	 * @return the id
	 */
	public Long getId() {

		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {

		return name;
	}

	public AUPVersion getOldestVersion() {

		if (versions.isEmpty())
			return null;

		return versions.first();
	}

	public Integer getReacceptancePeriod() {
		return reacceptancePeriod;
	}

	public AUPVersion getVersion(String versionNumber) {

		if (versions.isEmpty())
			return null;

		for (AUPVersion v : versions)
			if (v.getVersion().equals(versionNumber))
				return v;

		return null;
	}

	/**
	 * @return the versions
	 */
	public Set<AUPVersion> getVersions() {

		return versions;
	}

	@Override
	public int hashCode() {

		if (this.getId() != null)
			return getId().hashCode();

		return super.hashCode();
	}

	public void setActiveVersion(AUPVersion v) {

		if (versions.isEmpty())
			throw new NoSuchAUPVersionException(
					"This AUP has no versions defined currently, so you cannot set the 'active' version");

		if (versions.contains(v))
			v.setActive(true);
		else
			throw new NoSuchAUPVersionException(
					"AUP version '"
							+ v
							+ "' not found among the currently defined versions for this AUP.");

		for (AUPVersion vv : versions)
			if ((!vv.equals(v)) && vv.getActive())
				vv.setActive(false);

	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {

		this.description = description;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {

		this.name = name;
	}

	public void setReacceptancePeriod(Integer reacceptancePeriod) {
		this.reacceptancePeriod = reacceptancePeriod;
	}

	/**
	 * @param versions
	 *            the versions to set
	 */
	public void setVersions(SortedSet<AUPVersion> versions) {

		this.versions = versions;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		
		builder.append("id",id).append("name",name).append("description", description).append("reacceptancePeriod", reacceptancePeriod).append("versions",versions);
		
		return builder.toString();

	}

}
