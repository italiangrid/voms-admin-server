package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.operations.BaseVoReadOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;

public class ListExpiredUsersOperation extends BaseVoReadOperation {

	@Override
	protected Object doExecute() {

		return VOMSUserDAO.instance().findExpiredUsers();
	}
	
	protected ListExpiredUsersOperation() {}
	
	
	public static ListExpiredUsersOperation instance(){
		
		return new ListExpiredUsersOperation();
	}
	
}
