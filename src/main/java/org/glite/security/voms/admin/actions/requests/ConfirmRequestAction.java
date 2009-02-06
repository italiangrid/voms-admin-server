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
package org.glite.security.voms.admin.actions.requests;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.ConfirmRequestForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.NotFoundException;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.RequestDAO;
import org.glite.security.voms.admin.database.AlreadyConfirmedRequestException;
import org.glite.security.voms.admin.database.ConfirmationRequestException;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMembershipRequest;
import org.glite.security.voms.admin.notification.HandleRequestNotification;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class ConfirmRequestAction extends BaseAction {

    private static final Log log = LogFactory
            .getLog( ConfirmRequestAction.class );
    
    public ActionForward execute( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

        ConfirmRequestForm rForm = (ConfirmRequestForm) form;

        if (!VOMSConfiguration.instance().getBoolean( VOMSConfiguration.REGISTRATION_SERVICE_ENABLED, true))
            return mapping.findForward( "registrationDisabled" );
        
        Long id = rForm.getRequestId();
        VOMembershipRequest req = RequestDAO.instance().findById( id );

        if ( req == null )
            throw new NotFoundException( "Request with id:" + id
                    + " not found in database!" );

        log.debug( "req id:" + id );
        log.debug( "confirm id: {received: " + rForm.getConfirmId()
                + "} {expected: " + req.getConfirmId() + "}" );

        if ( !req.getStatus().equals( VOMembershipRequest.SUBMITTED ) )
            throw new AlreadyConfirmedRequestException( "Cannot confirm an already confirmed request!" );

        if ( rForm.getConfirmId().equals( req.getConfirmId() ) )
            req.setStatus( VOMembershipRequest.CONFIRMED );
        else
            throw new ConfirmationRequestException(
                    "Wrong confirmation id for request #:" + id );

        RequestDAO.instance().save( req );

        // Notify admins
        ActionForward fw = mapping.findForward( "pendingRequests" );
        String reqManURL = getBaseContext( request ) + fw.getPath();

        VOMSContext voContext = VOMSContext.getVoContext();
        Collection c = voContext.getACL().getAdminsWithPermissions(
                VOMSPermission.getRequestsRWPermissions() );
        
        HandleRequestNotification n = new HandleRequestNotification(req, reqManURL );
        
        if ( c.isEmpty() ) {
            
            log.debug( "No admins found with required permissions!" );

            String adminEmail = VOMSConfiguration.instance().getString(
                    VOMSConfiguration.SERVICE_EMAIL_ADDRESS );
            
            
            n.addRecipient( adminEmail );
            n.send();

        } else {

            Iterator i = c.iterator();
                    
            while ( i.hasNext() ) {
                
                VOMSAdmin a = (VOMSAdmin) i.next();
                n.addAdminToRecipients( a );
                
                
            }
            
            if (n.getRecipientList().isEmpty()){
                
                String adminEmail = VOMSConfiguration.instance().getString(
                        VOMSConfiguration.SERVICE_EMAIL_ADDRESS );
                
                n.addRecipient( adminEmail );
                
            }
            
            n.send();
        }
        
        return findSuccess( mapping );
    }

}