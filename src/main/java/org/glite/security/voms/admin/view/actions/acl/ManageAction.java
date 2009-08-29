package org.glite.security.voms.admin.view.actions.acl;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")

@Results({
	@Result(name=BaseAction.SUCCESS,location="aclManage"),
	@Result(name=BaseAction.INPUT, location="aclManage")
})
public class ManageAction extends ACLActionSupport{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Long aclGroupId = -1L;
	Long aclRoleId = -1L;
	
	Boolean showDefaultACL;
	
	@Override
	public String execute() throws Exception {
		
		if (getModel() == null){
			
			//
		}else{
			
			VOMSContext ctxt = model.getContext();
			
			if (model.isDefautlACL())
				showDefaultACL = true;
			
			aclGroupId = ctxt.getGroup().getId();
			if (!ctxt.isGroupContext())
				aclRoleId = ctxt.getRole().getId();
			
		}
		
		return SUCCESS;
	}
	

	public Long getAclGroupId() {
		return aclGroupId;
	}

	public void setAclGroupId(Long aclGroupId) {
		this.aclGroupId = aclGroupId;
	}

	public Long getAclRoleId() {
		return aclRoleId;
	}

	public void setAclRoleId(Long aclRoleId) {
		this.aclRoleId = aclRoleId;
	}

	public Boolean getShowDefaultACL() {
		return showDefaultACL;
	}

	public void setShowDefaultACL(Boolean showDefaultACL) {
		this.showDefaultACL = showDefaultACL;
	}
	
}
