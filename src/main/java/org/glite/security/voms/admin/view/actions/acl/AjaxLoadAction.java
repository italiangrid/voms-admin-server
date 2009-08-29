package org.glite.security.voms.admin.view.actions.acl;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Results({
	@Result(name=BaseAction.SUCCESS,location="aclDetail.jsp"),
	@Result(name=BaseAction.INPUT,location="aclManage.jsp")
})
public class AjaxLoadAction extends ACLActionSupport {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	Long aclGroupId = -1L;
	Long aclRoleId = -1L;
	
	Boolean showDefaultACL;
	
	VOMSContext vomsContext;
	
	@SkipValidation
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	@Override
	public void prepare() throws Exception {
		
		vomsContext = null;
		
		if (getModel() == null){
			if (aclGroupId != -1L && aclRoleId !=-1L)
				vomsContext = VOMSContext.instance(aclGroupId, aclRoleId);
			else if (aclGroupId != -1L)
				vomsContext = VOMSContext.instance(groupById(aclGroupId));
			
			
			if (vomsContext != null){
			
				if(showDefaultACL != null && vomsContext.isGroupContext())
					model = vomsContext.getGroup().getDefaultACL();
				else
					model = vomsContext.getACL();
			}
			
		}
		
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

	public VOMSContext getVomsContext() {
		return vomsContext;
	}

	public void setVomsContext(VOMSContext vomsContext) {
		this.vomsContext = vomsContext;
	}

	
		
}
