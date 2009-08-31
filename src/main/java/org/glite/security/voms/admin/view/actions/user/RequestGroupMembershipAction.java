package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.model.VOMSGroup;

@ParentPackage("base")
@Results({
	
	@Result(name=UserActionSupport.SUCCESS,location="mappingsRequest.jsp"),
	@Result(name=UserActionSupport.ERROR,location="mappingsRequest.jsp"),
	@Result(name=UserActionSupport.INPUT,location="mappingsRequest.jsp")
})
public class RequestGroupMembershipAction extends UserActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Long groupId;
	
	
	@Override
	public void validate() {
		
		
		
	}
	
	@Override
	public String execute() throws Exception {
		
		if (!VOMSConfiguration.instance().getBoolean(
				VOMSConfiguration.REGISTRATION_SERVICE_ENABLED, true))
			return "registrationDisabled";
		
		RequestDAO reqDAO = DAOFactory.instance().getRequestDAO();
		
		VOMSGroup g = groupById(groupId);
		
		addActionMessage("Ole'!");
		
//		if (g == null)
//			throw new NoSuchGroupException("Group with id '"+groupId+"' not found!");
//		
//		if (model.isMember(g)){
//			addActionError(getText("group_request.user.already_member", new String[]{model.toString(),g.getName()}));
//			return ERROR;
//		}
//		
//		if (reqDAO.userHasPendingGroupMembershipRequest(model, g)){
//			addActionError(getText("group_request.user.has_pending_request",new String[]{model.toString(),g.getName()}));
//			return ERROR;
//		}
//		
//		// FIXME: change duration before release!
//		GroupMembershipRequest req = reqDAO.createGroupMembershipRequest(getModel(), g, getFutureDate(new Date(), Calendar.MINUTE, 5));
//		
//		EventManager.dispatch(new GroupMembershipSubmittedEvent(req, getHomeURL()));
		
		return SUCCESS;
	}

	public Long getGroupId() {
		return groupId;
	}	
	
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}
