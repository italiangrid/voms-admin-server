package org.glite.security.voms.admin.core.validation.strategies.impl;

import java.util.Collections;
import java.util.List;

import org.glite.security.voms.admin.core.validation.strategies.ExpiredMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.ExpiringMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiredMembersStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleExpiringMembersStrategy;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class IgnoreMembershipEndTimeStrategy implements 
	HandleExpiredMembersStrategy, 
	ExpiredMembersLookupStrategy,
	ExpiringMembersLookupStrategy, 
	HandleExpiringMembersStrategy{

	
	public List<VOMSUser> findExpiredMembers() {
		return Collections.EMPTY_LIST;
	}

	public void handleExpiredMembers(List<VOMSUser> expiredMembers) {
		// Do NOTHING 
	}

	public void handleMembersAboutToExpire(List<VOMSUser> expiringMembers) {
		// Do NOTHING
	}

	public List<VOMSUser> findExpiringMembers() {
		
		return Collections.EMPTY_LIST;
	}


}
