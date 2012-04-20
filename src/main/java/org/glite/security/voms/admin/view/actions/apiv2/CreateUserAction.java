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
