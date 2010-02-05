package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestApprovedEvent;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestRejectedEvent;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.Request.STATUS;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.users.CreateUserOperation;

public class HandleVOMembershipRequest extends
		BaseHandleRequestOperation<NewVOMembershipRequest> {

	private static final String REJECT_MOTIVATION = "The VO administrator didn't find appropriate to approve your membership request.";

	public HandleVOMembershipRequest(NewVOMembershipRequest request,
			DECISION decision) {
		super(request, decision);

	}

	

	@Override
	protected void approve() {
		checkRequestStatus(STATUS.CONFIRMED);

		VOMSUser user = (VOMSUser) CreateUserOperation.instance(request)
				.execute();

		request.approve();

		// Add a sign aup record for the user
		VOMSUserDAO.instance().signAUP(user,
				DAOFactory.instance().getAUPDAO().getVOAUP());

		EventManager
				.dispatch(new VOMembershipRequestApprovedEvent(request));
		
	}


	@Override
	protected void reject() {
		request.reject();

		EventManager.dispatch(new VOMembershipRequestRejectedEvent(request,
				REJECT_MOTIVATION));

		DAOFactory.instance().getRequestDAO().makeTransient(request);
		
	}


	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
				.getContainerRWPermissions().setMembershipRWPermission()
				.setRequestsReadPermission().setRequestsWritePermission());

	}

}
