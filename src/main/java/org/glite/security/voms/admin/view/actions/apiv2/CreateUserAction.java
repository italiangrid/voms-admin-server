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

package org.glite.security.voms.admin.view.actions.apiv2;

import java.util.Collection;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.operations.users.CreateUserOperation;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@ParentPackage("json")
@Results( { @Result(name = BaseAction.SUCCESS, type = "json"),
	@Result(name = BaseAction.INPUT, type = "json") })
public class CreateUserAction  extends BaseAction implements ValidationAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	VOMSUserJSON user;
	String certificateSubject;
	String caSubject;
	

	@Override
	public String execute() throws Exception {
		
		CreateUserOperation op = CreateUserOperation.instance(user, certificateSubject, caSubject);
		VOMSUser newUser = (VOMSUser) op.execute();
		addActionMessage(String.format("User %s created succesfully.", newUser.getShortName(), newUser.getId()));
		
		return SUCCESS;
	}

	@JSON(serialize=true)
	@Override
	public Collection<String> getActionMessages() {
		
		return super.getActionMessages();
	}
	
	
	@VisitorFieldValidator(appendPrefix = true, message = "Invalid input:  ")
	public VOMSUserJSON getUser() {
		return user;
	}

	public void setUser(VOMSUserJSON user) {
		this.user = user;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please provide a certificate subject for the user.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The certificate subject contains illegal characters!", expression = "^[^<>&;]*$")
	@JSON(serialize=false)
	public String getCertificateSubject() {
		return certificateSubject;
	}

	public void setCertificateSubject(String certificateSubject) {
		this.certificateSubject = certificateSubject;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please provide a CA subject for the user.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The CA certificate subject contains illegal characters!", expression = "^[^<>&;]*$")
	@JSON(serialize=false)
	public String getCaSubject() {
		return caSubject;
	}

	public void setCaSubject(String caSubject) {
		this.caSubject = caSubject;
	}
}
