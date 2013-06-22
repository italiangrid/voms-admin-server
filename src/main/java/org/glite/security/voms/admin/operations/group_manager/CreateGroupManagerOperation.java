package org.glite.security.voms.admin.operations.group_manager;

import org.glite.security.voms.admin.persistence.model.GroupManager;


public class CreateGroupManagerOperation extends BaseGroupManagerOperation {

	public CreateGroupManagerOperation(GroupManager gm) {
		super(gm);
	}

	@Override
	protected Object doExecute() {
		dao.makePersistent(manager);
		return manager;
	}
	
}
