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


	public void validateRequest(NewVOMembershipRequest r) throws RequestValidationException {
		
		for (RequestValidator<NewVOMembershipRequest> validator: membershipRequestValidators)
			validator.validateRequest(r);
		
	}


	public void validateRequests(List<NewVOMembershipRequest> requests) throws RequestValidationException {
		for (RequestValidator<NewVOMembershipRequest> validator: membershipRequestValidators)
			validator.validateRequests(requests);
	}


	public void validateMembership(VOMSUser user) {
		// TODO Auto-generated method stub
		
	}


	public void validateMembership(List<VOMSUser> users) {
		// TODO Auto-generated method stub
		
	}

	
	
}
