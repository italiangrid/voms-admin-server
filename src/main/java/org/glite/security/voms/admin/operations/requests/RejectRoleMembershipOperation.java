package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.common.IllegalRequestStateException;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.RoleMembershipRejectedEvent;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class RejectRoleMembershipOperation extends BaseVomsOperation {

	RoleMembershipRequest request;
	
	public static RejectRoleMembershipOperation instance(RoleMembershipRequest req){
		return new RejectRoleMembershipOperation(req);
	}
	
	private RejectRoleMembershipOperation(RoleMembershipRequest req) {
		this.request = req;
	}
	
	
	@Override
	protected Object doExecute() {
		if (!request.getStatus().equals(StatusFlag.SUBMITTED))
			throw new IllegalRequestStateException(
					"Illegal state for request: " + request.getStatus());
		
		request.reject();
		EventManager.dispatch(new RoleMembershipRejectedEvent(request));
		
		return request;
		
	}

	@Override
	protected void setupPermissions() {
		VOMSGroup g = VOMSGroupDAO.instance()
		.findByName(request.getGroupName());

		if (g == null) {
			addRequiredPermissionOnAllGroups(VOMSPermission
					.getRequestsRWPermissions());
		} else {
			VOMSRole r = VOMSRoleDAO.instance().findByName(request.getRoleName());
			
			if (r != null){
				addRequiredPermissionOnPath(g, VOMSPermission.getContainerReadPermission());
				addRequiredPermission(VOMSContext.instance(g, r), VOMSPermission.getRequestsRWPermissions());
			}else{
				addRequiredPermissionOnPath(g, VOMSPermission.getContainerReadPermission());
				addRequiredPermission(VOMSContext.instance(g), VOMSPermission.getRequestsRWPermissions());
			}
		}
	}

}
