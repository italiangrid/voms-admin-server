package org.glite.security.voms.admin.operations.group_manager;

import org.glite.security.voms.admin.persistence.model.GroupManager;


public class DeleteGroupManagerOperation extends BaseGroupManagerOperation {

	public DeleteGroupManagerOperation(GroupManager m) {
		super(m);
	}

	@Override
	protected Object doExecute() {
		dao.makeTransient(manager);
		return manager;
	}

}
