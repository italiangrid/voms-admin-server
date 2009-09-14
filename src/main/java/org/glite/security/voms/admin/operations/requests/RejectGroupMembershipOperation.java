package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.common.IllegalRequestStateException;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.GroupMembershipRejectedEvent;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class RejectGroupMembershipOperation extends BaseVomsOperation {

	GroupMembershipRequest request;

	private RejectGroupMembershipOperation(GroupMembershipRequest req) {
		this.request = req;
	}

	public static RejectGroupMembershipOperation instance(
			GroupMembershipRequest req) {

		return new RejectGroupMembershipOperation(req);
	}

	@Override
	protected Object doExecute() {
		
		if (!request.getStatus().equals(StatusFlag.SUBMITTED))
			throw new IllegalRequestStateException(
					"Illegal state for request: " + request.getStatus());
		
		request.reject();
		EventManager.dispatch(new GroupMembershipRejectedEvent(request));
		
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

			addRequiredPermissionOnPath(g, VOMSPermission
					.getContainerReadPermission());
			addRequiredPermission(VOMSContext.instance(g), VOMSPermission
					.getRequestsRWPermissions());

		}
	}
}
