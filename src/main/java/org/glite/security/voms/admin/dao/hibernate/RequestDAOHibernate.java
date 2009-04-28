package org.glite.security.voms.admin.dao.hibernate;

import java.util.Date;
import java.util.UUID;

import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.model.request.CertificateRequest;
import org.glite.security.voms.admin.model.request.GroupMembershipRequest;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.request.RequesterInfo;
import org.glite.security.voms.admin.model.request.RoleMembershipRequest;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;


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



}
