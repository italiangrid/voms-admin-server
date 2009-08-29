package org.glite.security.voms.admin.view.actions.role;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.roles.CreateRoleOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")

@Results({
	@Result(name=BaseAction.SUCCESS,location="/role/search.action", type="redirect"),
	@Result(name=BaseAction.INPUT, location="roleCreate")
})

@Action(value="create", interceptorRefs={@InterceptorRef(value="authenticatedStack", params={"tokenSession.includeMethods", "execute"})})
public class CreateAction extends RoleActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	VOMSRole role;
	String roleName;
	
	public VOMSRole getModel() {
		
		return role;
	}

	public void prepare() throws Exception {
		
		
	}

	
	public String execute() throws Exception {
		
		VOMSRole r = (VOMSRole) CreateRoleOperation.instance(getRoleName()).execute();
		if (r != null)
			addActionMessage(getText("confirm.role.creation", r.getName()));
	
		return SUCCESS;
	}

	
	@RequiredStringValidator(type=ValidatorType.FIELD,message="A name for the role is required!")
	@RegexFieldValidator(type=ValidatorType.FIELD, message="The role name field contains illegal characters!", expression="^[^<>&=;]*$")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	
}
