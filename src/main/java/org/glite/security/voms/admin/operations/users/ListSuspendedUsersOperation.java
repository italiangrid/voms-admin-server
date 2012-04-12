package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.operations.BaseVoReadOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;

public class ListSuspendedUsersOperation extends BaseVoReadOperation {

	@Override
	protected Object doExecute() {
		return VOMSUserDAO.instance().findSuspendedUsers();
	}

	protected ListSuspendedUsersOperation() {}
	
	public static ListSuspendedUsersOperation instance(){
		return new ListSuspendedUsersOperation();
	}
}
