package org.glite.security.voms.admin.view.actions.user;

import java.util.List;

import org.glite.security.voms.admin.view.actions.BaseAction;

public class UserBulkActionSupport extends BaseAction {
	
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
	
	
	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	
}
