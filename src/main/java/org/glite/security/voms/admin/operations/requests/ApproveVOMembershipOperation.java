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

import java.util.Date;

import org.glite.security.voms.admin.common.IllegalRequestStateException;
import org.glite.security.voms.admin.dao.RequestDAO;
import org.glite.security.voms.admin.database.NoSuchRequestException;
import org.glite.security.voms.admin.model.VOMembershipRequest;
import org.glite.security.voms.admin.notification.RequestApprovedNotification;
import org.glite.security.voms.admin.operations.users.CreateUserOperation;


public class ApproveVOMembershipOperation extends RequestRWOperation {

    Long requestId;
    
    private ApproveVOMembershipOperation(Long reqId) {

        requestId = reqId;
    }
    
    protected Object doExecute() {

        VOMembershipRequest req = (VOMembershipRequest) LoadVOMembershipRequestOperation.instance( requestId ).execute();
        
        if (req == null)
            throw new NoSuchRequestException("No request found with id "+requestId);
        
        if (!req.getStatus().equals( VOMembershipRequest.CONFIRMED ))
            throw new IllegalRequestStateException("Illegal state for request!");
        
        CreateUserOperation.instance( req.getDn(), req.getCa(), req.getCn(), null, req.getEmailAddress() ).execute();
        
        req.setStatus( VOMembershipRequest.APPROVED );
        req.setEvaluationDate( new Date() );
        RequestDAO.instance().save( req );
        
        // Notify user.
        RequestApprovedNotification n = new RequestApprovedNotification(req.getEmailAddress());
        n.send();
        
        return req;
    }

    public static ApproveVOMembershipOperation instance(Long reqId) {

        return new ApproveVOMembershipOperation(reqId);
    }
}
