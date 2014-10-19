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
package org.glite.security.voms.admin.api.registration;

import java.io.Serializable;

/**
 * This class models a VOMS registration request.
 * 
 * @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a>
 *
 */
public class RegistrationRequest implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	/**
	 * A flag that states whether the AUP was accepted by the user.
	 */
	boolean aupAccepted;

	/**
	 * The user email address
	 */
	String emailAddress;
	/**
	 * The user institute
	 */
	String institute;
	/**
	 * The user phone number
	 */
	String phoneNumber;
	
	/**
	 * Comments to the VO administrator.
	 */
	String comments;

	public boolean isAupAccepted() {

		return aupAccepted;
	}

	public void setAupAccepted(boolean aupAccepted) {

		this.aupAccepted = aupAccepted;
	}

	public String getComments() {

		return comments;
	}

	public void setComments(String comments) {

		this.comments = comments;
	}

	public String getEmailAddress() {

		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {

		this.emailAddress = emailAddress;
	}

	public String getInstitute() {

		return institute;
	}

	public void setInstitute(String institute) {

		this.institute = institute;
	}

	public String getPhoneNumber() {

		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {

		this.phoneNumber = phoneNumber;
	}

	public RegistrationRequest() {

		setAupAccepted(false);
	}
}
