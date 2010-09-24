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

package org.glite.security.voms.admin.integration.orgdb;

import java.util.List;
import java.util.Vector;

import org.glite.security.voms.admin.core.validation.RequestValidationContext;
import org.glite.security.voms.admin.core.validation.RequestValidationResult;
import org.glite.security.voms.admin.core.validation.strategies.RequestValidationStrategy;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBVOMSPersonDAO;
import org.glite.security.voms.admin.integration.orgdb.database.OrgDBError;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSOrgDBPerson;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;

public class OrgDBRequestValidator implements RequestValidationStrategy<NewVOMembershipRequest>, RequestValidationContext{

	String experimentName;

	public OrgDBRequestValidator(String experimentName) {
		this.experimentName = experimentName;
	}

	protected void propertyEqualsIgnoreCase(String value1, String value2, String propertyName, List<String> errors){
		
		if (!value1.equalsIgnoreCase(value2)){
			
			String errorMessage = String.format("Property '"+propertyName+"' does not match (ignoring case) the OrgDB VOMS person record. You entered  '%s', while  '%s' was expected.",
					value1, value2);
			errors.add(errorMessage);
		}
		
	}
	protected List<String> checkRequestAgainstParticipation(NewVOMembershipRequest r, VOMSOrgDBPerson p){
		
		List<String> errors = new Vector<String>();
		
		propertyEqualsIgnoreCase(r.getRequesterInfo().getName(), p.getFirstName(), "name", errors);
		propertyEqualsIgnoreCase(r.getRequesterInfo().getSurname(), p.getName(), "surname", errors);
				
		return errors;
		
	}
	public RequestValidationResult validateRequest(NewVOMembershipRequest r) {

		try {

			String email = r.getRequesterInfo().getEmailAddress();
			OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance()
					.getVOMSPersonDAO();

			VOMSOrgDBPerson p = dao
					.findPersonWithValidExperimentParticipationByEmail(email,
							experimentName);

			if (p != null) {
				
				List<String> errors = checkRequestAgainstParticipation(r, p);
				
				if (!errors.isEmpty()){
					RequestValidationResult result = RequestValidationResult.failure("OrgDb validation failed. The OrgDb VOMS person record linked to email address '"+email+"' did not match the data you entered.");
					result.setErrorMessages(errors);
					return result;
				}
				else{
					return RequestValidationResult.success();
				}
			
			} else {

				RequestValidationResult result = RequestValidationResult
						.failure("No OrgDB participation found matching email '"
								+ email
								+ "' for experiment '"
								+ experimentName
								+ "'.");
				
				return result;
			}

		} catch (OrgDBError e) {
			return RequestValidationResult.error(e.getMessage(), e);
		}
	}

	public String getExperimentName() {
		return experimentName;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}


	public RequestValidationStrategy<NewVOMembershipRequest> getVOMembershipRequestValidationStrategy() {
		
		return this;
	}

}
