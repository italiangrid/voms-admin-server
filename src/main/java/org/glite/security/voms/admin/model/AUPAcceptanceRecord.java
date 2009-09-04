package org.glite.security.voms.admin.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class AUPAcceptanceRecord implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	Long id;

	AUPVersion aupVersion;

	VOMSUser user;

	Date lastAcceptanceDate;

	public AUPAcceptanceRecord(VOMSUser u, AUPVersion aup) {
		this.user = u;
		this.aupVersion = aup;
	}

	public AUPAcceptanceRecord() {

		// TODO Auto-generated constructor stub
	}

	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof AUPAcceptanceRecord))
			return false;

		if (other == null)
			return false;

		AUPAcceptanceRecord that = (AUPAcceptanceRecord) other;

		// Implement meaningful checks here
		return (getUser().equals(that.getUser()) && getAupVersion().equals(
				that.getAupVersion()));

	}

	@Override
	public int hashCode() {

		if (getId() != null)
			return getId().hashCode();

		return super.hashCode();
	}

	/**
	 * @return the id
	 */
	public Long getId() {

		return id;
	}

	/**
	 * @return the aupVersion
	 */
	public AUPVersion getAupVersion() {

		return aupVersion;
	}

	/**
	 * @return the user
	 */
	public VOMSUser getUser() {

		return user;
	}

	/**
	 * @return the lastAcceptanceDate
	 */
	public Date getLastAcceptanceDate() {

		return lastAcceptanceDate;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @param aupVersion
	 *            the aupVersion to set
	 */
	public void setAupVersion(AUPVersion aupVersion) {

		this.aupVersion = aupVersion;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(VOMSUser user) {

		this.user = user;
	}

	/**
	 * @param lastAcceptanceDate
	 *            the lastAcceptanceDate to set
	 */
	public void setLastAcceptanceDate(Date lastAcceptanceDate) {

		this.lastAcceptanceDate = lastAcceptanceDate;
	}

	@Override
	public String toString() {
		
		return String.format(
				"[user: %s, aupVersion: %s, lastAcceptanceDate: %s]", user,
				aupVersion, lastAcceptanceDate);
	}
	
	
	public boolean hasExpired(){
		
		if (lastAcceptanceDate.before(aupVersion.getLastUpdateTime()))
			return true;
					
		Date now = new Date();
		if (getExpirationDate().before(now))
			return true;
		
		return false;
	}
	
	
	public Date getExpirationDate(){
		
		Calendar c = Calendar.getInstance();
		c.setTime(lastAcceptanceDate);
		
		c.add(Calendar.DAY_OF_YEAR, aupVersion.getAup().getReacceptancePeriod());
		
		return c.getTime();
	}
	

}
