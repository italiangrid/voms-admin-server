package org.glite.security.voms.admin.view.actions.apiv2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.operations.users.RestoreUserOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("json")
@Results( { @Result(name = BaseAction.SUCCESS, type = "json") })
public class RestoreAllSuspendedUsersAction extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<VOMSUserJSON> restoredUsers;
	
	@Override
	public String execute() throws Exception {
		
		restoredUsers = new ArrayList<VOMSUserJSON>();
		
		List<VOMSUser> suspendedUsers = VOMSUserDAO.instance().findSuspendedUsers();
		
		for (VOMSUser u: suspendedUsers){
			
			RestoreUserOperation.instance(u).execute();
			restoredUsers.add(VOMSUserJSON.fromVOMSUser(u));
		}
		
		return SUCCESS;
	}
	
	@JSON(serialize=true)
	public Collection<String> getActionMessages() {
		
		return super.getActionMessages();
	}

	@JSON(serialize=true)
	public List<VOMSUserJSON> getRestoredUsers() {
		return restoredUsers;
	}
	
}
