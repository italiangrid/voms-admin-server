package org.glite.security.voms.admin.view.actions.user;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.SessionAware;
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
		ModelDriven<VOMSUser>, Preparable, SessionAware {

	public static final Log log = LogFactory.getLog(UserActionSupport.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String LOAD_THIS_USER_KEY = "loadUserId";
	
	Long userId = -1L;

	VOMSUser model;

	protected List<Request> requests;;
	
	protected List<GroupMembershipRequest> pendingGroupMembershipRequests;

	protected List<RoleMembershipRequest> pendingRoleMembershipRequests;
	
	protected Map<String, Object> theSession;
	
	public VOMSUser getModel() {

		return model;
	}

	public void prepare() throws Exception {

		if (getModel() == null) {

			if (getUserId() != -1)
				model = userById(getUserId());		
			else{
				Long loadThisUserKey = (Long) theSession.get(LOAD_THIS_USER_KEY);
				
				if (loadThisUserKey != null){
					model = userById(loadThisUserKey);
					theSession.remove(LOAD_THIS_USER_KEY);
				}
				
			}
		}
			
		if (getModel()!= null)
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

	public void setSession(Map<String, Object> session) {
		
		log.debug("Setting session: "+session);
		this.theSession = session;
		
		
	}
	
	
	
}
