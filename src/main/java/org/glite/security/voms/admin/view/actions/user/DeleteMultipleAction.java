package org.glite.security.voms.admin.view.actions.user;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results({
	
	@Result(name = BaseAction.SUCCESS, location = "search", type = "redirectAction"),
	@Result(name = BaseAction.INPUT, location = "users")
})


public class DeleteMultipleAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<Long> userIds;
	
	@Override
	public void validate() {
		
		if (userIds == null)
			addActionError("No users seelected!");
		else if (userIds.contains("ognl.NoConversionPossible"))
			addActionError("No users seelected!");
	}
	
	
	@Override
	public String execute() throws Exception {
		
		return SUCCESS;
	}


	public List<Long> getUserIds() {
		return userIds;
	}


	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	
}
