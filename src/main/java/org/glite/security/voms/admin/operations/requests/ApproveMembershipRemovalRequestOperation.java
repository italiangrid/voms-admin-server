package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.common.IllegalRequestStateException;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.MembershipRemovalApprovedEvent;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.users.DeleteUserOperation;
import org.glite.security.voms.admin.operations.users.FindUserOperation;

public class ApproveMembershipRemovalRequestOperation extends BaseVomsOperation {

	MembershipRemovalRequest request;
	
	private ApproveMembershipRemovalRequestOperation(MembershipRemovalRequest req) {
		
		this.request = req;
		
	}
	
	public static ApproveMembershipRemovalRequestOperation instance(MembershipRemovalRequest req){
		return new ApproveMembershipRemovalRequestOperation(req);
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
		
		DeleteUserOperation.instance(u).execute();
		
		request.approve();
		
		EventManager.dispatch(new MembershipRemovalApprovedEvent(request));
		
		return u;
	}

	@Override
	protected void setupPermissions() {
		
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerRWPermissions().setMembershipRWPermission());

	}

}
