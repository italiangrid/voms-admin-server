package org.glite.security.voms.admin.core.validation.strategies.impl;

import java.util.List;

import org.glite.security.voms.admin.core.validation.strategies.ExpiredMembersLookupStrategy;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class DefaultExpiredMembersLookupStrategy implements
		ExpiredMembersLookupStrategy {

	public List<VOMSUser> findExpiredMembers() {
		
		return VOMSUserDAO.instance().findExpiredUsers();

	}

}
