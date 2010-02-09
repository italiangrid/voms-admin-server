package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.CertificateRequestApprovedEvent;
import org.glite.security.voms.admin.event.registration.CertificateRequestRejectedEvent;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.CertificateRequest;
import org.glite.security.voms.admin.model.request.Request.STATUS;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.users.AddUserCertificateOperation;

public class HandleCertificateRequestOperation extends BaseHandleRequestOperation<CertificateRequest> {
	
	public HandleCertificateRequestOperation(CertificateRequest request,
			DECISION decision) {
		super(request, decision);
	}

	@Override
	protected void approve() {
		
		checkRequestStatus(STATUS.SUBMITTED);
		VOMSUser u = getRequesterAsVomsUser();
		
		AddUserCertificateOperation.instance(u, request.getCertificateSubject(), request.getCertificateIssuer(), null).execute();
		request.approve();
		EventManager.dispatch(new CertificateRequestApprovedEvent(request));
		
		
	}

	@Override
	protected void reject() {
		
		checkRequestStatus(STATUS.SUBMITTED);
		request.reject();
		EventManager.dispatch(new CertificateRequestRejectedEvent(request));
		
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
				.getContainerRWPermissions().setMembershipRWPermission()
				.setRequestsReadPermission().setRequestsWritePermission());

	}

	
}
