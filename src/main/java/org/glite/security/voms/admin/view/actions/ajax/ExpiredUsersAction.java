package org.glite.security.voms.admin.view.actions.ajax;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.apiv2.JSONSerializer;
import org.glite.security.voms.admin.apiv2.SimpleVOMSUser;
import org.glite.security.voms.admin.operations.users.ListExpiredUsersOperation;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("json")
@Results( { @Result(name = BaseAction.SUCCESS, type = "json") })
public class ExpiredUsersAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<SimpleVOMSUser> expiredUsers;

	@Override
	public String execute() throws Exception {
		
		expiredUsers = JSONSerializer.serialize((List<VOMSUser>)ListExpiredUsersOperation.instance().execute());
		
		return SUCCESS;
	}
	
	public List<SimpleVOMSUser> getExpiredUsers() {
		return expiredUsers;
	}
	
	
}
