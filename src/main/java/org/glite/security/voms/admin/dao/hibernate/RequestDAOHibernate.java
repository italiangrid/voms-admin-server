package org.glite.security.voms.admin.dao.hibernate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.CertificateRequest;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.request.RequesterInfo;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.model.task.Task.TaskStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

public class RequestDAOHibernate extends GenericHibernateDAO<Request, Long>
		implements RequestDAO {

	public CertificateRequest createCertificateRequest() {

		// TODO Auto-generated method stub
		return null;
	}

	public NewVOMembershipRequest createVOMembershipRequest(
			RequesterInfo requester, Date expirationDate) {

		NewVOMembershipRequest req = new NewVOMembershipRequest();

		req.setStatus(StatusFlag.SUBMITTED);
		req.setRequesterInfo(requester);
		req.setCreationDate(new Date());
		req.setExpirationDate(expirationDate);

		req.setConfirmId(UUID.randomUUID().toString());
		makePersistent(req);

		return req;
	}

	public NewVOMembershipRequest findActiveVOMembershipRequest(
			RequesterInfo requester) {
		Criteria crit = getSession().createCriteria(
				NewVOMembershipRequest.class);

		crit.add(Restrictions.ne("status", StatusFlag.APPROVED)).add(
				Restrictions.ne("status", StatusFlag.REJECTED)).createCriteria(
				"requesterInfo").add(
				Restrictions.eq("certificateSubject", requester
						.getCertificateSubject())).add(
				Restrictions.eq("certificateIssuer", requester
						.getCertificateIssuer())).add(
				Restrictions.eq("emailAddress", requester.getEmailAddress()));

		return (NewVOMembershipRequest) crit.uniqueResult();

	}

	public List<NewVOMembershipRequest> findConfirmedVOMembershipRequests() {

		Criteria crit = getSession().createCriteria(
				NewVOMembershipRequest.class);

		crit.add(Restrictions.eq("status", StatusFlag.CONFIRMED));

		return crit.list();

	}

	public boolean userHasPendingGroupMembershipRequest(VOMSUser u, VOMSGroup g){
		
		
		Criteria crit = getSession().createCriteria(
				GroupMembershipRequest.class);
		
		Disjunction dnEqualityChecks = Restrictions.disjunction();
		
		for (Certificate c: u.getCertificates())
			dnEqualityChecks.add(Restrictions.eq("certificateSubject", c.getSubjectString()));
		
		
		crit.add(Restrictions.eq("groupName", g.getName()));
		
		crit.add(Restrictions.disjunction().
				add(Restrictions.eq("status", StatusFlag.SUBMITTED)).
				add(Restrictions.eq("status", StatusFlag.PENDING))).createCriteria("requesterInfo").add(dnEqualityChecks);
				
				
		List<GroupMembershipRequest> reqs= crit.list();
		
		if (reqs ==  null || reqs.isEmpty())
			return false;
		
		
		return true;
	}
	
	public GroupMembershipRequest createGroupMembershipRequest(VOMSUser usr,
			VOMSGroup group, Date expirationDate) {
		
		if (usr.isMember(group))
			throw new VOMSException("User '"+usr+"' is already member of group '"+group+"'!");
		
		if (userHasPendingGroupMembershipRequest(usr, group))
			throw new VOMSException("User '"+usr+"' has a pending group membership request for group '"+group+"'!");
		
		GroupMembershipRequest req = new GroupMembershipRequest();
		req.setStatus(StatusFlag.SUBMITTED);
		req.setRequesterInfo(RequesterInfo.fromVOUser(usr));
		req.setCreationDate(new Date());
		req.setExpirationDate(expirationDate);
		
		req.setGroupName(group.getName());
		
		makePersistent(req);
		
		return req;
	}

	public List<GroupMembershipRequest> findPendingUserGroupMembershipRequests(VOMSUser u){
		
		Criteria crit = getSession().createCriteria(
				GroupMembershipRequest.class);
		
		Disjunction dnEqualityChecks = Restrictions.disjunction();
		
		for (Certificate c: u.getCertificates())
			dnEqualityChecks.add(Restrictions.eq("certificateSubject", c.getSubjectString()));
		
		crit.add(Restrictions.disjunction().
				add(Restrictions.eq("status", StatusFlag.SUBMITTED)).
				add(Restrictions.eq("status", StatusFlag.PENDING))).createCriteria("requesterInfo").add(dnEqualityChecks);
				
		
		return crit.list();
	}
	
	public List<RoleMembershipRequest> findPendingUserRoleMembershipRequests(VOMSUser u){
		return null;
	}
	
	
	public RoleMembershipRequest createRoleMembershipRequest(VOMSUser usr,
			VOMSGroup group, VOMSRole r, Date expirationDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
