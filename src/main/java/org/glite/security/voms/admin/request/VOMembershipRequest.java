/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/

package org.glite.security.voms.admin.request;

import java.util.Calendar;
import java.util.Date;

import org.glite.security.voms.admin.common.VOMSConfiguration;

public class VOMembershipRequest extends AbstractRequest {

	public VOMembershipRequest() {

	}

	public Date getExpirationTime() {

		int expirationTimeInMinutes = VOMSConfiguration.instance().getInt(
				VOMSConfiguration.VO_MEMBERSHIP_EXPIRATION_TIME);

		Calendar c = Calendar.getInstance();
		c.setTime(creationTime);
		c.add(Calendar.MINUTE, expirationTimeInMinutes);

		return c.getTime();

	}

	public boolean hasExpired() {

		Date now = new Date();
		return (getExpirationTime().compareTo(now) > 0);
	}

	public State getCurrentState() {

		return stateMachine.getCurrentState();
	}

	public State process(Event e) {

		return stateMachine.process(e);
	}

	public boolean inFinalState() {

		return stateMachine.inFinalState();
	}

	public Date getCreationTime() {

		return creationTime;
	}

	public void setCreationTime(Date creationTime) {

		this.creationTime = creationTime;
	}

}
