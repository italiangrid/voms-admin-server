package org.glite.security.voms.admin.view.actions.role;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.SingleArgumentOperationCollection;
import org.glite.security.voms.admin.operations.roles.DeleteRoleOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.ConversionErrorFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results({
	
	@Result(name = BaseAction.SUCCESS, location = "/role/search.action", type = "redirect"),
	@Result(name = BaseAction.INPUT, location = "search", type = "chain")
})
public class DeleteMultipleAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<Long> roleIds;

	@Override
	public void validate() {
		
		if (roleIds ==  null)
			addActionError("No roles selected!");
		else if (roleIds.contains("ognl.NoConversionPossible"))
			addActionError("No roles selected!");
			
		
	}
	
	@Override
	public String execute() throws Exception {
		
		SingleArgumentOperationCollection<Long> deleteOps = new SingleArgumentOperationCollection<Long>(roleIds, DeleteRoleOperation.class);
		
		deleteOps.execute();
			
		return SUCCESS;
	}

	
	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}
	
}
