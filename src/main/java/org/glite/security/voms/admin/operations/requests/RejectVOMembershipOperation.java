/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Authors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.operations.requests;

import org.glite.security.voms.admin.common.IllegalRequestStateException;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.database.NoSuchRequestException;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.registration.VOMembershipRequestRejectedEvent;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;

public class RejectVOMembershipOperation extends RequestRWOperation {

	NewVOMembershipRequest req;

	private RejectVOMembershipOperation(NewVOMembershipRequest request) {

		this.req = request;

	}

	protected Object doExecute() {

		if (!req.getStatus().equals(StatusFlag.CONFIRMED))
			throw new IllegalRequestStateException("Illegal state for request!");

		req.reject();

		EventManager
				.dispatch(new VOMembershipRequestRejectedEvent(
						req,
						"The VO administrator didn't find appropriate to approve your membership request."));
		
		// Remove rejected membership requests from database
		DAOFactory.instance().getRequestDAO().makeTransient(req);

		return req;
	}

	public static RejectVOMembershipOperation instance(
			NewVOMembershipRequest req) {

		return new RejectVOMembershipOperation(req);
	}
}
