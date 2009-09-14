package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.SingleArgumentOperationCollection;
import org.glite.security.voms.admin.operations.users.RestoreUserOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results({
	
	@Result(name = BaseAction.SUCCESS, location = "search", type = "redirectAction"),
	@Result(name = BaseAction.INPUT, location = "users")
})
public class BulkRestoreAction extends UserBulkActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Override
	public String execute() throws Exception {
		SingleArgumentOperationCollection<Long> op = new SingleArgumentOperationCollection<Long>(userIds, RestoreUserOperation.class);
		op.execute();
		return SUCCESS;
	}
	
}
