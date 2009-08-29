package org.glite.security.voms.admin.view.actions.acl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.acls.SaveACLEntryOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")

@Results({
	@Result(name=BaseAction.SUCCESS,location="manage", type="chain"),
	@Result(name=BaseAction.INPUT, location="editACLEntry")
})
public class EditEntryAction extends ACLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<String> selectedPermissions;
	VOMSPermission permission;
		
	@Override
	public String execute() throws Exception {
		
		String permString;
		
		if (selectedPermissions == null)
			permString = "NONE";
		else if (selectedPermissions.contains("ALL"))
			permString = "ALL";
		else
			permString = StringUtils.join(selectedPermissions,"|");
		
		SaveACLEntryOperation op = SaveACLEntryOperation.instance(getModel(), 
                admin, 
                VOMSPermission.fromString( permString ),
                propagate);                
		
		op.execute();
		
		return SUCCESS;
	}
	
	public void prepareInput() throws Exception{
		prepare();
		
		if (permission == null)
			permission = getModel().getPermissions(getAdmin());
		
	}

	public VOMSPermission getPermission() {
		return permission;
	}

	public void setPermission(VOMSPermission permission) {
		this.permission = permission;
	}

	public List<String> getSelectedPermissions() {
		return selectedPermissions;
	}

	public void setSelectedPermissions(List<String> selectedPermissions) {
		this.selectedPermissions = selectedPermissions;
	}
	
	

}
