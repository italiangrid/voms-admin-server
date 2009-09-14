package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.common.IllegalRequestStateException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.MembershipRemovalRejectedEvent;
import org.glite.security.voms.admin.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class RejectMembershipRemovalRequestOperation extends BaseVomsOperation {

	MembershipRemovalRequest request;
	
	private RejectMembershipRemovalRequestOperation(MembershipRemovalRequest req) {
		this.request = req;
	}
	
	public static RejectMembershipRemovalRequestOperation instance(MembershipRemovalRequest req){
		return new RejectMembershipRemovalRequestOperation(req);
	}
	
	
	@Override
	protected Object doExecute() {
		
		if (!request.getStatus().equals(StatusFlag.SUBMITTED))
			throw new IllegalRequestStateException(
					"Illegal state for request: " + request.getStatus());
		
		request.reject();
		
		EventManager.dispatch(new MembershipRemovalRejectedEvent(request));
		return request;
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());

	}

}
