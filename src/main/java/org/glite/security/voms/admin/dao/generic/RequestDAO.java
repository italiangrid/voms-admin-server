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
package org.glite.security.voms.admin.dao.generic;

import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.request.CertificateRequest;
import org.glite.security.voms.admin.persistence.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.glite.security.voms.admin.persistence.model.request.RoleMembershipRequest;

public interface RequestDAO extends GenericDAO<Request, Long> {

	public CertificateRequest createCertificateRequest(VOMSUser u, String certificateSubject, String certificateIssuer, Date expirationDate);

	public GroupMembershipRequest createGroupMembershipRequest(VOMSUser usr, VOMSGroup group, Date expirationDate);

	public MembershipRemovalRequest createMembershipRemovalRequest(VOMSUser usr, String reason, Date expirationDate);

	public RoleMembershipRequest createRoleMembershipRequest(VOMSUser usr, VOMSGroup group, VOMSRole r, Date expirationDate);
	
	public NewVOMembershipRequest createVOMembershipRequest(
			RequesterInfo requester, Date expirationDate);

	public void deleteRequestFromUser(VOMSUser u);

	public NewVOMembershipRequest findActiveVOMembershipRequest(
			RequesterInfo requester);
	
	public List<NewVOMembershipRequest> findConfirmedVOMembershipRequests();
	
	public List<NewVOMembershipRequest> findExpiredVOMembershipRequests();
	public List<CertificateRequest> findPendingCertificateRequests();
	
	public List<GroupMembershipRequest> findPendingGroupMembershipRequests();
	
	public List<MembershipRemovalRequest> findPendingMembershipRemovalRequests();
	
	public List<Request> findPendingRequests();
	
	public List<RoleMembershipRequest> findPendingRoleMembershipRequests();
	
	public List<CertificateRequest> findPendingUserCertificateRequests(VOMSUser u);
	public List<GroupMembershipRequest> findPendingUserGroupMembershipRequests(VOMSUser u);
	public List<MembershipRemovalRequest> findPendingUserMembershipRemovalRequests(VOMSUser u);
	public List<RoleMembershipRequest> findPendingUserRoleMembershipRequests(VOMSUser u);
	
	public List<NewVOMembershipRequest> findRejectedVOMembershipRequests();
	
	public List<Request> findRequestsFromUser(VOMSUser u);
	
	public boolean userHasPendingCertificateRequest(VOMSUser u, String certificateSubject, String certificateIssuer);
	
	public boolean userHasPendingGroupMembershipRequest(VOMSUser u, VOMSGroup g);
	
	public boolean userHasPendingMembershipRemovalRequest(VOMSUser u);
	
	public boolean userHasPendingRoleMembershipRequest(VOMSUser u, VOMSGroup g, VOMSRole r);
	
}
