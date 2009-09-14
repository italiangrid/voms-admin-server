package org.glite.security.voms.admin.view.actions.user;

import java.util.Collections;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.DoubleArgumentOperationCollection;
import org.glite.security.voms.admin.operations.users.SuspendUserOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results({
	
	@Result(name = BaseAction.SUCCESS, location = "search", type = "redirectAction"),
	@Result(name = BaseAction.INPUT, location = "users")
})
public class BulkSuspendAction extends UserBulkActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	String suspensionReason;
	

	public String getSuspensionReason() {
		return suspensionReason;
	}

	public void setSuspensionReason(String suspensionReason) {
		this.suspensionReason = suspensionReason;
	}
	
	
	@Override
	public String execute() throws Exception {
		
		List<String> reasonsList = Collections.nCopies(userIds.size(), suspensionReason);
		DoubleArgumentOperationCollection<Long, String> op = new DoubleArgumentOperationCollection<Long, String>(userIds, 
				reasonsList,
				SuspendUserOperation.class);
		
		op.execute();
		return SUCCESS;
	}
}
