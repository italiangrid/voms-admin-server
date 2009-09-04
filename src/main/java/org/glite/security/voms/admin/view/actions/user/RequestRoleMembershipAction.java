package org.glite.security.voms.admin.view.actions.user;

import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.database.NoSuchRoleException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.RoleMembershipSubmittedEvent;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;

@ParentPackage("base")
@Results({
	
	@Result(name=UserActionSupport.SUCCESS,location="mappingsRequest.jsp"),
	@Result(name=UserActionSupport.ERROR,location="mappingsRequest.jsp"),
	@Result(name=UserActionSupport.INPUT,location="mappingsRequest.jsp")
})
public class RequestRoleMembershipAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Long groupId;
	Long roleId;
	
	@Override
	public void validate() {
		
		RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
		
		VOMSGroup g = groupById(groupId);
		VOMSRole r = roleById(roleId);
		
		if (g == null)
			throw new NoSuchGroupException("Group with id '"+groupId+"' not found!");
		
		if (r == null)
			throw new NoSuchRoleException("Role with id '"+roleId+"' not found!");
		
		
		if (model.hasRole(g, r))
			addActionError(getText("role_request.user.already_member", new String[]{model.toString(), r.getName(), g.getName()}));
		
		if (reqDAO.userHasPendingRoleMembershipRequest(model, g, r))
			addActionError(getText("role_request.user.has_pending_request", new String[]{model.toString(), r.getName(), g.getName()}));
		
	}
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	@Override
	public String execute() throws Exception {
		
		if (!VOMSConfiguration.instance().getBoolean(
				VOMSConfiguration.REGISTRATION_SERVICE_ENABLED, true))
			return "registrationDisabled";
		
		if (hasActionErrors())
			return ERROR;
		
		RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
		
		VOMSGroup g = groupById(groupId);
		VOMSRole r = roleById(roleId);
		
		RoleMembershipRequest request = reqDAO.createRoleMembershipRequest(model, g, r, getFutureDate(new Date(), Calendar.DAY_OF_YEAR, 5));
		EventManager.dispatch(new RoleMembershipSubmittedEvent(request, getHomeURL()));
		
		refreshPendingRequests();
		
		return SUCCESS;
	}
}
