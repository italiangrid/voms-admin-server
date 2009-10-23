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
