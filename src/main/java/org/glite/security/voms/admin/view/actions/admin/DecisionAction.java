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
package org.glite.security.voms.admin.view.actions.admin;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.database.UserAlreadyExistsException;
import org.glite.security.voms.admin.model.request.CertificateRequest;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.operations.requests.DECISION;
import org.glite.security.voms.admin.operations.requests.HandleCertificateRequestOperation;
import org.glite.security.voms.admin.operations.requests.HandleGroupRequestOperation;
import org.glite.security.voms.admin.operations.requests.HandleMembershipRemovalRequest;
import org.glite.security.voms.admin.operations.requests.HandleRoleMembershipRequestOperation;
import org.glite.security.voms.admin.operations.requests.HandleVOMembershipRequest;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "pendingRequests.jsp"),
		@Result(name = BaseAction.INPUT, location = "pendingRequests.jsp") })
public class DecisionAction extends RequestActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String decision;

	
	@Override
	public String execute() throws Exception {
		
		DECISION theDecision = DECISION.valueOf(decision.toUpperCase());

		if (request instanceof NewVOMembershipRequest) {

			try{
				new HandleVOMembershipRequest((NewVOMembershipRequest)request, theDecision).execute();
			
			}catch(UserAlreadyExistsException e){
				
				//FIXME: should be implemented in the validate method!
				addActionError("A user with such certificate already exists. Please reject such request.");
				return INPUT;
			}
			
		}
		
		if (request instanceof GroupMembershipRequest)
			new HandleGroupRequestOperation((GroupMembershipRequest)request, theDecision).execute();
			
		
		if (request instanceof RoleMembershipRequest)
			new HandleRoleMembershipRequestOperation((RoleMembershipRequest)request, theDecision).execute();

		
		if (request instanceof MembershipRemovalRequest)
			new HandleMembershipRemovalRequest((MembershipRemovalRequest)request,theDecision).execute();
		
		if (request instanceof CertificateRequest)
			new HandleCertificateRequestOperation((CertificateRequest)request, theDecision).execute();
		
		
			
		refreshPendingRequests();
		
		setDecision(null);
		
		return SUCCESS;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

}
