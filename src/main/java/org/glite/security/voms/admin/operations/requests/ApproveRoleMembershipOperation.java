package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.common.IllegalRequestStateException;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.database.NoSuchRoleException;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.GroupMembershipApprovedEvent;
import org.glite.security.voms.admin.event.registration.RoleMembershipApprovedEvent;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.groups.AddMemberOperation;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.roles.FindRoleOperation;
import org.glite.security.voms.admin.operations.users.AssignRoleOperation;
import org.glite.security.voms.admin.operations.users.FindUserOperation;

public class ApproveRoleMembershipOperation extends BaseVomsOperation {

	RoleMembershipRequest request;
	
	private ApproveRoleMembershipOperation(RoleMembershipRequest req) {
		this.request = req;
	}
	
	public static ApproveRoleMembershipOperation instance(RoleMembershipRequest req){
		
		return new ApproveRoleMembershipOperation(req);
	}
	
	
	@Override
	protected Object doExecute() {
		if (!request.getStatus().equals(StatusFlag.SUBMITTED))
			throw new IllegalRequestStateException(
					"Illegal state for request: " + request.getStatus());
		
		// Get the VOMS user
		VOMSUser u = (VOMSUser) FindUserOperation.instance(
				request.getRequesterInfo().getCertificateSubject(),
				request.getRequesterInfo().getCertificateIssuer()).execute();
		
		if (u == null)
			throw new NoSuchUserException(String.format(
					"User '%s, %s' not found!", request.getRequesterInfo()
							.getCertificateSubject(), request
							.getRequesterInfo().getCertificateIssuer()));
		
		
		VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(request.getGroupName()).execute();
		VOMSRole r = (VOMSRole) FindRoleOperation.instance(request.getRoleName()).execute();
		
		if (g == null)
			throw new NoSuchGroupException("Group named '"+request.getGroupName()+"' not found!");
		
		if (r == null)
			throw new NoSuchRoleException("Requested role '"+request.getRoleName()+"' does not exist in this VO.");
		
		AssignRoleOperation.instance(u, g, r).execute();
		
		request.approve();
		
		EventManager.dispatch(new RoleMembershipApprovedEvent(request));
		
		return request;
	}

	@Override
	protected void setupPermissions() {
		
		VOMSGroup g  = VOMSGroupDAO.instance().findByName(request.getGroupName());
		
		if (g == null)
			throw new NoSuchGroupException("Requested group '"+request.getGroupName()+"' does not exist in this VO.");
		
		
		VOMSRole r = VOMSRoleDAO.instance().findByName(request.getRoleName());
		
		if (r == null)
			throw new NoSuchRoleException("Requested role '"+request.getRoleName()+"' does not exist in this VO.");
		
		addRequiredPermissionOnPath(g, VOMSPermission.getContainerReadPermission());
		
		addRequiredPermission(VOMSContext.instance(g, r), VOMSPermission.getMembershipRWPermissions());
		addRequiredPermission(VOMSContext.instance(g, r), VOMSPermission.getRequestsRWPermissions());
	}

}
