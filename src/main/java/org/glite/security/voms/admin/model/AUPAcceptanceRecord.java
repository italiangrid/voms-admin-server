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
