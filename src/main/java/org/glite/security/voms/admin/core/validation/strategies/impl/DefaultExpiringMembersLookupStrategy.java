package org.glite.security.voms.admin.core.validation.strategies.impl;

import java.util.List;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.core.validation.strategies.ExpiringMembersLookupStrategy;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExpiringMembersLookupStrategy implements
		ExpiringMembersLookupStrategy {

	public static final Logger log = LoggerFactory
			.getLogger(DefaultExpiringMembersLookupStrategy.class);


	public List<VOMSUser> findExpiringMembers() {
		
		return VOMSUserDAO.instance().findExpiringUsers(VOMSConfiguration.instance().getExpiringUsersWarningInterval());
				
	}

}
