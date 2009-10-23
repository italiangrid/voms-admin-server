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
package org.glite.security.voms.admin.notification.messages;

import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;

public class MembershipRequestOutcome extends AbstractVelocityNotification {

	Request request;
	
	public MembershipRequestOutcome(Request req) {
		this.request = req;
	}
	
	@Override
	protected void buildMessage() {
		
		String voName = VOMSConfiguration.instance().getVOName();
		
		if (request instanceof GroupMembershipRequest){
			
			GroupMembershipRequest groupReq = (GroupMembershipRequest)request;
			setSubject("Your membership request for group '"+groupReq.getGroupName()+"' has been "+groupReq.getStatus().toString());
			context.put("context", groupReq.getGroupName());
			
		}else if (request instanceof RoleMembershipRequest){
			RoleMembershipRequest roleReq = (RoleMembershipRequest)request;
			setSubject("Your membership request for role '"+roleReq.getFQAN()+"' has been "+roleReq.getStatus().toString());
			context.put("context", roleReq.getFQAN());
		}
		
		context.put("req", request);
		context.put("voName", voName);
		
		super.buildMessage();
	}
	
}
