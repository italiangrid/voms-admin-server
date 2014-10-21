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

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.users.CreateUserOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


@Results({
	@Result(name=UserActionSupport.INPUT, location="userCreate" ),
	@Result(name=UserActionSupport.SUCCESS, location="load", type="redirectAction")
})
@InterceptorRef(value = "authenticatedStack", params = {
		"token.includeMethods", "execute" })
public class CreateAction extends UserActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String theName;
	String theSurname;
	String theInstitution;
	String theAddress;
	String thePhoneNumber;
	String theEmailAddress;
	
	String subject;
	String caSubject;
	
	@Override
	public String execute() throws Exception {
		
		if (getModel() != null)
			return EDIT;
		
		VOMSUser newUser = new VOMSUser();
		newUser.setName(theName);
		newUser.setSurname(theSurname);
		newUser.setInstitution(theInstitution);
		newUser.setAddress(theAddress);
		newUser.setPhoneNumber(thePhoneNumber);
		newUser.setEmailAddress(theEmailAddress);
		newUser.setDn(subject.trim());
		
		CreateUserOperation op = CreateUserOperation.instance(newUser, caSubject);
		model= (VOMSUser) op.execute();
		
		// Add a Sign AUP record for this user
		VOMSUserDAO.instance().signAUP(model, DAOFactory.instance().getAUPDAO().getVOAUP());
		
		theSession.put(LOAD_THIS_USER_KEY, model.getId());
		
		return SUCCESS;
	}

	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The name field contains illegal characters!", expression = "^[^<>&=;]*$")
	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter a name for the user.")
	public String getTheName() {
		return theName;
	}


	public void setTheName(String theName) {
		this.theName = theName;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter a family name for the user.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The surname field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getTheSurname() {
		return theSurname;
	}


	public void setTheSurname(String theSurname) {
		this.theSurname = theSurname;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter an institution for the user.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The institution field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getTheInstitution() {
		return theInstitution;
	}


	public void setTheInstitution(String theInstitution) {
		this.theInstitution = theInstitution;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter an address for the user.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The address field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getTheAddress() {
		return theAddress;
	}


	public void setTheAddress(String theAddress) {
		this.theAddress = theAddress;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter a phoneNumber for the user.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The phoneNumber field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getThePhoneNumber() {
		return thePhoneNumber;
	}


	public void setThePhoneNumber(String thePhoneNumber) {
		this.thePhoneNumber = thePhoneNumber;
	}


	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter an email address for the user.")
	@EmailValidator(type = ValidatorType.FIELD, message = "Please enter a valid email address.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The email field name contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getTheEmailAddress() {
		return theEmailAddress;
	}


	public void setTheEmailAddress(String theEmailAddress) {
		this.theEmailAddress = theEmailAddress;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter a certificate subject for the user.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The subject field name contains illegal characters!", expression = "^[^<>&;]*$")
	public String getSubject() {	
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCaSubject() {
		return caSubject;
	}

	public void setCaSubject(String caSubject) {
		this.caSubject = caSubject;
	}

	@Override
	public void validate() {
		VOMSUser candidate  = VOMSUserDAO.instance().findByDNandCA(subject, caSubject);
		
		if (candidate != null){
			addFieldError("subject", "A user having this certificate already exists in the VO!");
			addFieldError("caSubject", "A user having this certificate already exists in the VO!");
		}
		
	}
	
	
	

}
