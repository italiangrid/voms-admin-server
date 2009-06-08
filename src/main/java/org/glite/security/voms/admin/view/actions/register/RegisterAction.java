package org.glite.security.voms.admin.view.actions.register;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.INPUT,location="register"),
	@Result(name=BaseAction.SUCCESS,location="registerConfirmation")
})

public class RegisterAction extends BaseAction implements Preparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	RegistrationRequest request;
	
	public void prepare() throws Exception {
		
		
	}
	
	@Override
	public String execute() throws Exception {
		
		
		
		return SUCCESS;
	}

	public RegistrationRequest getRequest() {
		return request;
	}

	public void setRequest(RegistrationRequest request) {
		this.request = request;
	}
	
	

}
