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

package org.glite.security.voms.admin.integration;

import java.util.ArrayList;
import java.util.List;

import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;

public class ValidationManager implements RequestValidator<NewVOMembershipRequest>, MembershipValidator{
	
	
	private static ValidationManager INSTANCE = null;
	
	protected List<RequestValidator<NewVOMembershipRequest>> membershipRequestValidators;
	protected List<MembershipValidator> membershipValidatorPlugins;
	
	private ValidationManager() {
		
		membershipRequestValidators = new ArrayList<RequestValidator<NewVOMembershipRequest>>();
		membershipValidatorPlugins = new ArrayList<MembershipValidator>();
	}
	
	
	public static ValidationManager instance(){
		
		if (INSTANCE == null)
			INSTANCE = new ValidationManager();
		
		return INSTANCE;
	}
	
	
	public void registerRequestValidator(RequestValidator<NewVOMembershipRequest> validator){
		
		membershipRequestValidators.add(validator);
	}
	
	public void registerMembershipValidator(MembershipValidator validator){
		
		membershipValidatorPlugins.add(validator);
	}


	public ValidationResult validateRequest(NewVOMembershipRequest r) {
		
		if (membershipRequestValidators.isEmpty())
			return ValidationResult.success();
		
		return membershipRequestValidators.get(0).validateRequest(r);
		
	}


	public ValidationResult validateMembership(VOMSUser user) {
		// TODO Auto-generated method stub
		return null;
	}


	public ValidationResult validateMembership(List<VOMSUser> users) {
		// TODO Auto-generated method stub
		return null;
	}

	



	
	
}
