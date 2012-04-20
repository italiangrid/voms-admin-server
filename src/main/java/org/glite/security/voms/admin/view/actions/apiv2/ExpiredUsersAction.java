package org.glite.security.voms.admin.view.actions.apiv2;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.apiv2.JSONSerializer;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
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
	
	List<VOMSUserJSON> expiredUsers;

	@Override
	public String execute() throws Exception {
		
		expiredUsers = JSONSerializer.serialize((List<VOMSUser>)ListExpiredUsersOperation.instance().execute());
		
		return SUCCESS;
	}
	
	public List<VOMSUserJSON> getExpiredUsers() {
		return expiredUsers;
	}
	
	
}
