package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.GroupMembershipApprovedEvent;
import org.glite.security.voms.admin.event.registration.GroupMembershipRejectedEvent;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.Request.STATUS;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.groups.AddMemberOperation;

public class HandleGroupRequestOperation extends BaseHandleRequestOperation<GroupMembershipRequest> {

	public HandleGroupRequestOperation(GroupMembershipRequest request, DECISION decision) {
		super(request, decision);
	}


	@Override
	protected void approve() {
		
		checkRequestStatus(STATUS.SUBMITTED);
		
		VOMSUser u = getRequesterAsVomsUser();
		VOMSGroup g = findGroupByName(request.getGroupName());
		
		AddMemberOperation.instance(u, g).execute();
		
		request.approve();
		
		EventManager.dispatch(new GroupMembershipApprovedEvent(request));
		
	
	}

	@Override
	protected void reject() {
		
		checkRequestStatus(STATUS.SUBMITTED);
		request.reject();
		EventManager.dispatch(new GroupMembershipRejectedEvent(request));
	}

	@Override
	protected void setupPermissions() {
		
		VOMSGroup g  = findGroupByName(request.getGroupName());
		addRequiredPermissionOnPath(g, VOMSPermission.getContainerReadPermission());
		addRequiredPermission(VOMSContext.instance(g), VOMSPermission.getMembershipRWPermissions());
		addRequiredPermission(VOMSContext.instance(g), VOMSPermission.getRequestsRWPermissions());

	}

}
