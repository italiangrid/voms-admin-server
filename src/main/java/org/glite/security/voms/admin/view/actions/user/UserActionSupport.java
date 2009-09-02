package org.glite.security.voms.admin.view.actions.user;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
public class UserActionSupport extends BaseAction implements
		ModelDriven<VOMSUser>, Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long userId = -1L;

	VOMSUser model;

	protected List<Request> requests;;
	
	protected List<GroupMembershipRequest> pendingGroupMembershipRequests;

	protected List<RoleMembershipRequest> pendingRoleMembershipRequests;

	public VOMSUser getModel() {

		return model;
	}

	public void prepare() throws Exception {

		if (getModel() == null) {

			if (getUserId() != -1){
				model = userById(getUserId());
				refreshPendingRequests();	
			}
		}else
			refreshPendingRequests();

	}
		
		

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<GroupMembershipRequest> getPendingGroupMembershipRequests() {
		return pendingGroupMembershipRequests;
	}

	public List<RoleMembershipRequest> getPendingRoleMembershipRequests() {
		return pendingRoleMembershipRequests;
	}
	
	protected void refreshPendingRequests(){
		
		RequestDAO rDAO = DAOFactory.instance().getRequestDAO();
		
		requests = rDAO.findRequestsFromUser(model);
		
		pendingGroupMembershipRequests = rDAO.findPendingUserGroupMembershipRequests(model);
		
		pendingRoleMembershipRequests = rDAO.findPendingUserRoleMembershipRequests(model);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Request> getRequests() {
		return requests;
	}
	
	
	
}
