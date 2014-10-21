/**
 * Copyright (c) INFN 2006-2014.
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
package org.glite.security.voms.admin.integration.orgdb;

import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBVOMSPersonDAO;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSOrgDBPerson;
import org.glite.security.voms.admin.integration.orgdb.strategies.OrgDBEmailAddressValidationStrategy;
import org.glite.security.voms.admin.integration.orgdb.strategies.OrgDBEmailValidationResult;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class DefaultEmailValidationStrategy implements
	OrgDBEmailAddressValidationStrategy {

	private final String experimentName;

	public DefaultEmailValidationStrategy(String experiment) {

		experimentName = experiment;
	}

	private void sanityChecks(VOMSUser u, String emailAddress) {

		if (u == null)
			throw new IllegalArgumentException("null voms user");

		if (emailAddress == null)
			throw new IllegalArgumentException("null emailAddress");

		if (emailAddress.isEmpty())
			throw new IllegalArgumentException("Empty email address");
	}

	@Override
	public OrgDBEmailValidationResult validateEmailAddress(VOMSUser u, String emailAddress) {
		sanityChecks(u, emailAddress);
		
		OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance()
			.getVOMSPersonDAO();
		
		VOMSOrgDBPerson person = 
			dao.findPersonWithValidExperimentParticipationByEmail(emailAddress,experimentName);
		
		if (person == null){
			
			String msg = String.format("No record found in OrgDB for emailAddress '%s' in experiment '%s'.",
				emailAddress, experimentName);
			
			return OrgDBEmailValidationResult.invalid(msg);
		}
		
		if (!person.getFirstName().equalsIgnoreCase(u.getName()) || 
			!person.getName().equalsIgnoreCase(u.getSurname())){
			
			String msg = String.format("Name in OrgDB record linked with email '%s' does not match '%s'.",
				emailAddress, u.getFullName());
			
			return OrgDBEmailValidationResult.invalid(msg);	
			
		}
		
		return OrgDBEmailValidationResult.valid();
	}
}
