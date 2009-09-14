package org.glite.security.voms.admin.dao.generic;

import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.CertificateRequest;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.MembershipRemovalRequest;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.request.RequesterInfo;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;

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
