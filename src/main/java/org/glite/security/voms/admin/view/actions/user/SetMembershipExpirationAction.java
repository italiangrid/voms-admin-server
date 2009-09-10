package org.glite.security.voms.admin.view.actions.user;

import java.util.Date;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.users.SetMembershipExpirationOperation;

import com.opensymphony.xwork2.validator.annotations.DateRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


@ParentPackage("base")
@Results({
	@Result(name=UserActionSupport.SUCCESS,location = "membershipExpiration.jsp"),
	@Result(name=UserActionSupport.INPUT, location="membershipExpiration.jsp")
})	
public class SetMembershipExpirationAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Date expirationDate;
	
	
	@Override
	public void validate() {
		
		Date now = new Date();
		
		if (now.after(expirationDate))
			addFieldError("expirationDate", "Please enter a future expiration date for the user.");
		
		return;
		
	}
	
	
	@Override
	public String execute() throws Exception {
		
		SetMembershipExpirationOperation op = new SetMembershipExpirationOperation(getModel(), expirationDate);
		op.execute();
		
		addActionMessage("Membership expiration date changed.");
		return SUCCESS;
	}

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "Please set a membership expiration date for the user.")
	@DateRangeFieldValidator(type= ValidatorType.FIELD, message = "Please enter a valid expiration date for the user.")
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	
}
