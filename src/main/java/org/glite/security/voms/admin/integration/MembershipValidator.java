package org.glite.security.voms.admin.integration;

import java.util.List;

import org.glite.security.voms.admin.persistence.model.VOMSUser;

public interface MembershipValidator {
	
	public void validateMembership(VOMSUser user);
	
	public void validateMembership(List<VOMSUser> users);

}
