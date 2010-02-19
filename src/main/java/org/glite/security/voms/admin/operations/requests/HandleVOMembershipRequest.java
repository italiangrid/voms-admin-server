/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

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
