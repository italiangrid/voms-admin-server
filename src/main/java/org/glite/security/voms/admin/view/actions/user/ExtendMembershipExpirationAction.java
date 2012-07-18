package org.glite.security.voms.admin.view.actions.user;

import java.util.Date;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.core.validation.ValidationUtil;
import org.glite.security.voms.admin.operations.users.SetMembershipExpirationOperation;


@Results({
	@Result(name=UserActionSupport.SUCCESS,location = "membershipExpiration2.jsp"),
	@Result(name=UserActionSupport.INPUT, location="membershipExpiration2.jsp")
})
public class ExtendMembershipExpirationAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String execute() throws Exception {
		
		Date expirationDate = ValidationUtil.membershipExpirationDateStartingFromNow();
		
		SetMembershipExpirationOperation op = new SetMembershipExpirationOperation(getModel(), expirationDate);
		op.execute();
		
		addActionMessage("Membership expiration date extended.");
				
		return SUCCESS;
	}

}
