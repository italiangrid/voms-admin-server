package org.glite.security.voms.admin.core.validation.strategies.impl;

import java.util.List;

import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreserveExpiredMembersStrategy extends
		GracePeriodExpiredMembersStrategy {

	public static final Logger log = LoggerFactory.getLogger(PreserveExpiredMembersStrategy.class);
	
	public PreserveExpiredMembersStrategy(int notificationInterval) {
		super(notificationInterval);
	}
	
	@Override
	protected void suspendExpiredMembers(List<VOMSUser> expiredMembers) {
		
		if (expiredMembers.isEmpty()){	
			log.debug("No expired members found.");
			return;
		}
	
	}
}
