package org.glite.security.voms.admin.dao.generic;

import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.CertificateRequest;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.request.RequesterInfo;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;

public interface RequestDAO extends GenericDAO<Request, Long> {

	public NewVOMembershipRequest createVOMembershipRequest(
			RequesterInfo requester, Date expirationDate);

	public GroupMembershipRequest createGroupMembershipRequest(VOMSUser usr, VOMSGroup group, Date expirationDate);

	public RoleMembershipRequest createRoleMembershipRequest(VOMSUser usr, VOMSGroup group, VOMSRole r, Date expirationDate);

	public CertificateRequest createCertificateRequest();

	public NewVOMembershipRequest findActiveVOMembershipRequest(
			RequesterInfo requester);

	public List<NewVOMembershipRequest> findConfirmedVOMembershipRequests();
	
	public List<GroupMembershipRequest> findPendingUserGroupMembershipRequests(VOMSUser u);
	
	public List<RoleMembershipRequest> findPendingUserRoleMembershipRequests(VOMSUser u);

	public boolean userHasPendingGroupMembershipRequest(VOMSUser u, VOMSGroup g);
}
