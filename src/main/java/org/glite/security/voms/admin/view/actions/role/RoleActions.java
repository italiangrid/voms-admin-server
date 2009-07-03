package org.glite.security.voms.admin.view.actions.role;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.roles.DeleteRoleOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")

@Results({
	@Result(name=BaseAction.SUCCESS,location="/role/search.action", type="redirect"),
	@Result(name=BaseAction.INPUT, location="roleCreate")
})


public class RoleActions extends BaseAction {
	
	public static final Log log = LogFactory.getLog(RoleActions.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Long roleId;
	String roleName;
	
	
	@Action(value="delete", interceptorRefs={@InterceptorRef(value="authenticatedStack", params={"tokenSession.includeMethods", "*"})})
	public String delete() throws Exception{
		
		log.debug(String.format("roleId: %d, roleName: %s", getRoleId(), getRoleName()));
		
		
		VOMSRole r = (VOMSRole) DeleteRoleOperation.instance(getRoleId()).execute();
		
		if (r != null)
			addActionMessage(getText("confirm.role.deletion", r.getName()));
	
		return SUCCESS;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
