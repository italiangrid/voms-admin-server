package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.MembershipRemovalApprovedEvent;
import org.glite.security.voms.admin.event.registration.MembershipRemovalRejectedEvent;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.model.request.Request.STATUS;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.users.DeleteUserOperation;

public class HandleMembershipRemovalRequest extends
		BaseHandleRequestOperation<MembershipRemovalRequest> {

	public HandleMembershipRemovalRequest(MembershipRemovalRequest request,
			DECISION decision) {
		super(request, decision);
	}

	@Override
	protected void approve() {
		
		checkRequestStatus(STATUS.SUBMITTED);
		VOMSUser u = getRequesterAsVomsUser();
		DeleteUserOperation.instance(u).execute();
		
		request.approve();
		
		EventManager.dispatch(new MembershipRemovalApprovedEvent(request));
		

	}

	@Override
	protected void reject() {
		checkRequestStatus(STATUS.SUBMITTED);
		request.reject();
		EventManager.dispatch(new MembershipRemovalRejectedEvent(request));
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());
	}

}
