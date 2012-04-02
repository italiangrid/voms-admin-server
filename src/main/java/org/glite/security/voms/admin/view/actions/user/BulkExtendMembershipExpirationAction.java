package org.glite.security.voms.admin.view.actions.user;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.core.validation.ValidationUtil;
import org.glite.security.voms.admin.operations.DoubleArgumentOperationCollection;
import org.glite.security.voms.admin.operations.SingleArgumentOperationCollection;
import org.glite.security.voms.admin.operations.users.ConditionalRestoreUserOperation;
import org.glite.security.voms.admin.operations.users.SetMembershipExpirationOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;



@ParentPackage("base")

@Results({
	
	@Result(name = BaseAction.SUCCESS, location = "search", type = "redirectAction"),
	@Result(name = BaseAction.INPUT, location = "users")
})

@InterceptorRef(value = "authenticatedStack", params = {
		"token.includeMethods", "execute" })
public class BulkExtendMembershipExpirationAction extends UserBulkActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String execute() throws Exception {
		
		Date expirationDate = ValidationUtil.membershipExpirationDateStartingFromNow();
		List<Date> dates = Collections.nCopies(userIds.size(), expirationDate);
		
		DoubleArgumentOperationCollection<Long, Date> op = new DoubleArgumentOperationCollection<Long, Date>(userIds, 
					dates,SetMembershipExpirationOperation.class);
				
		op.execute();
		
		SingleArgumentOperationCollection<Long> restore = new SingleArgumentOperationCollection<Long>(userIds, ConditionalRestoreUserOperation.class);
		restore.execute();
		
		return SUCCESS;
	}

}
