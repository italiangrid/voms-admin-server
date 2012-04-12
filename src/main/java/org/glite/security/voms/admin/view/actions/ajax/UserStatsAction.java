package org.glite.security.voms.admin.view.actions.ajax;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("json")
@Results( { @Result(name = BaseAction.SUCCESS, type = "json") })
public class UserStatsAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	Long usersCount;
	Long suspendedUsersCount;
	Long expiredUsersCount;
	
	
	@Override
	public String execute() throws Exception {
		
		VOMSUserDAO dao = VOMSUserDAO.instance();
		
		usersCount = dao.countUsers();
		suspendedUsersCount = dao.countSuspendedUsers();
		expiredUsersCount = dao.countExpiredUsers();
		
		return super.execute();
	}

	
	public Long getSuspendedUsersCount() {
		return suspendedUsersCount;
	}


	public void setSuspendedUsersCount(Long suspendedUsersCount) {
		this.suspendedUsersCount = suspendedUsersCount;
	}


	public Long getExpiredUsersCount() {
		return expiredUsersCount;
	}


	public void setExpiredUsersCount(Long expiredUsersCount) {
		this.expiredUsersCount = expiredUsersCount;
	}


	public Long getUsersCount() {
		return usersCount;
	}


	public void setUsersCount(Long usersCount) {
		this.usersCount = usersCount;
	}
		
}
