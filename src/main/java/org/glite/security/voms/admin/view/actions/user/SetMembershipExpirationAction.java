/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
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
		
		if (expirationDate == null)
			addFieldError("expirationDate", "Please enter a valid expiration date for the user.");
		else if (now.after(expirationDate))
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
	@DateRangeFieldValidator(type = ValidatorType.FIELD, message = "Please enter a valid expiration date for the user.", min="12/25/2010")
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	
}
