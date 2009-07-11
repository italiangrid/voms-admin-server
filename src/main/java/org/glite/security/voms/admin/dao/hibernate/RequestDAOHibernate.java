package org.glite.security.voms.admin.dao.hibernate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.model.request.CertificateRequest;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.request.RequesterInfo;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.model.task.Task.TaskStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;


public class RequestDAOHibernate extends GenericHibernateDAO<Request, Long> 
    implements RequestDAO
{

    public CertificateRequest createCertificateRequest() {

        // TODO Auto-generated method stub
        return null;
    }

    public GroupMembershipRequest createGroupMembershipRequest() {

        // TODO Auto-generated method stub
        return null;
    }

    public RoleMembershipRequest createRoleMembershipRequest() {

        // TODO Auto-generated method stub
        return null;
    }

    public NewVOMembershipRequest createVOMembershipRequest(RequesterInfo requester, Date expirationDate) {

        NewVOMembershipRequest req = new NewVOMembershipRequest();
        
        req.setStatus( StatusFlag.SUBMITTED );
        req.setRequesterInfo( requester );
        req.setCreationDate( new Date());
        req.setExpirationDate( expirationDate );       
        
        req.setConfirmId( UUID.randomUUID().toString() );
        makePersistent( req );
        
        return req;
    }

	

	public NewVOMembershipRequest findActiveVOMembershipRequest(RequesterInfo requester){
		Criteria crit = getSession().createCriteria(NewVOMembershipRequest.class);
		
		crit.add(Restrictions.ne("status", StatusFlag.APPROVED)).
			add(Restrictions.ne("status", StatusFlag.REJECTED)).
			createCriteria("requesterInfo").
			add(Restrictions.eq("certificateSubject", requester.getCertificateSubject())).
			add(Restrictions.eq("certificateIssuer", requester.getCertificateIssuer())).
			add(Restrictions.eq("emailAddress", requester.getEmailAddress()));
		
		return (NewVOMembershipRequest) crit.uniqueResult();
		
	}

	public List<NewVOMembershipRequest> findConfirmedVOMembershipRequests() {
		
		Criteria crit = getSession().createCriteria(NewVOMembershipRequest.class);
		
		crit.add(Restrictions.eq("status", StatusFlag.CONFIRMED));
		
		return crit.list();
	
	}
    

}
