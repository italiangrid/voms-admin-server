package org.glite.security.voms.admin.dao.generic;

import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.model.request.CertificateRequest;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.request.RequesterInfo;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;

public interface RequestDAO extends GenericDAO<Request, Long> {

	public NewVOMembershipRequest createVOMembershipRequest(
			RequesterInfo requester, Date expirationDate);

	public GroupMembershipRequest createGroupMembershipRequest();

	public RoleMembershipRequest createRoleMembershipRequest();

	public CertificateRequest createCertificateRequest();

	public NewVOMembershipRequest findActiveVOMembershipRequest(
			RequesterInfo requester);

	public List<NewVOMembershipRequest> findConfirmedVOMembershipRequests();

}
