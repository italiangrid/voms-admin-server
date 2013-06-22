package org.glite.security.voms.admin.operations.group_manager;

import org.glite.security.voms.admin.persistence.model.GroupManager;


public class SaveGroupManagerOperation extends BaseGroupManagerOperation {

	
	protected SaveGroupManagerOperation(GroupManager m) {
		super(m);
	}

	@Override
	protected Object doExecute() {

		dao.makePersistent(getManager());
		return getManager();
	}

}
