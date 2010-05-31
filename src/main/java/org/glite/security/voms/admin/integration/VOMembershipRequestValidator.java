package org.glite.security.voms.admin.integration;

import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;

public interface VOMembershipRequestValidator {

	public void validate(NewVOMembershipRequest voMembershipRequest);
	
}
