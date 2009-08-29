package org.glite.security.voms.admin.view.actions.ajax;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("json")
@Results({
	@Result(name = BaseAction.SUCCESS, type="json")
})
public class PermissionAction extends BaseAction {

	public static final Log log = LogFactory.getLog(PermissionAction.class); 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String context;
	
	String permissionString;
	
	Long groupId;
	Long roleId;
	
	Boolean hasPermission;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Boolean getHasPermission() {
		return hasPermission;
	}

	public void setHasPermission(Boolean hasPermission) {
		this.hasPermission = hasPermission;
	}
	
	public String getPermissionString() {
		return permissionString;
	}

	public void setPermissionString(String permissionString) {
		this.permissionString = permissionString;
	}

	@Override
	public String execute() throws Exception {
		
		CurrentAdmin admin = CurrentAdmin.instance();
		VOMSContext  ctxt = null;
		
		if (context != null)
			ctxt = VOMSContext.instance(context);
		else{
			ctxt = VOMSContext.instance(groupId, roleId);
		}
		
		VOMSPermission perm = VOMSPermission.fromString(permissionString);
		
		log.debug("context: "+ctxt);
		log.debug("permission: "+perm);
		
		hasPermission = admin.hasPermissions(ctxt, perm);
		
		return SUCCESS;
	}
	
}
