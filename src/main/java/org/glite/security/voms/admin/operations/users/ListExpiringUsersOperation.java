package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.operations.BaseVoReadOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;

public class ListExpiringUsersOperation extends BaseVoReadOperation {

	@Override
	protected Object doExecute() {
	
		return VOMSUserDAO.instance().findExpiringUsers(VOMSConfiguration.instance().getExpiringUsersWarningInterval());
	}

	protected ListExpiringUsersOperation() {}
	
	public static ListExpiringUsersOperation instance(){
		
		return new ListExpiringUsersOperation();
	}
}
