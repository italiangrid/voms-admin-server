/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2007. 
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
package org.glite.security.voms.admin.service;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.VOMSException;
import org.glite.security.voms.admin.common.DNUtil;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.dao.RequestDAO;
import org.glite.security.voms.admin.database.AlreadyExistsException;
import org.glite.security.voms.admin.model.VOMembershipRequest;
import org.glite.security.voms.admin.notification.ConfirmRequest;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.request.VOMSNotificationException;
import org.glite.security.voms.service.registration.RegistrationRequest;
import org.glite.security.voms.service.registration.VOMSRegistration;


public class VOMSRegistrationService implements VOMSRegistration {

    private static final Log log = LogFactory
            .getLog( VOMSRegistrationService.class );
    
    private String buildConfirmURL(HttpServletRequest request, VOMembershipRequest membReq){
        
        return ServiceUtils.getBaseContext(request )+"/ConfirmVOMembershipRequest.do?requestId="+membReq.getId()+"&confirmId="+membReq.getConfirmId(); 
   }
   
   private String buildCancelURL(HttpServletRequest request, VOMembershipRequest membReq){
       
       return ServiceUtils.getBaseContext( request )+"/CancelVOMembershipRequest.do?requestId="+membReq.getId()+"&confirmId="+membReq.getConfirmId(); 
  }
    
    public void submitRegistrationRequest( RegistrationRequest request )
            throws RemoteException , VOMSException {

        try {

            HttpServletRequest httpServletRequest = (HttpServletRequest) MessageContext
                    .getCurrentContext().getProperty(
                            HTTPConstants.MC_HTTP_SERVLETREQUEST );

            if ( request == null )
                throw new NullArgumentException(
                        "Cannot submit a null request!" );

            CurrentAdmin admin = CurrentAdmin.instance();

            VOMembershipRequest req = RequestDAO.instance().findPendingForUser(
                    admin.getRealSubject(), admin.getRealIssuer() );

            if ( req != null )
                throw new AlreadyExistsException(
                        "A registration pending for user '"
                                + admin.getRealSubject()
                                + "' has already been received. "
                                + "Follow the instructions that were communicated to complete registration." );

            req = RequestDAO.instance().createFromAdmin(
                    request.getEmailAddress() );

            // Notify user
            String confirmURL = buildConfirmURL( httpServletRequest, req );
            String cancelURL = buildCancelURL( httpServletRequest, req );

            ConfirmRequest n = new ConfirmRequest(
                    request.getEmailAddress(), confirmURL, cancelURL );

            try {

                n.send();

            } catch ( VOMSNotificationException e ) {

                log.error( "Error sending notification for request!", e );
                log.error( "Request will be discarded." );
                RequestDAO.instance().delete( req );
            }

        } catch ( RuntimeException e ) {

            ServiceExceptionHelper.handleServiceException( log, e );
            throw e;
        }

    }

    public void submitRegistrationRequestForUser( String userSubject,
            String caSubject, RegistrationRequest request )
            throws RemoteException , VOMSException {

        try {

            HttpServletRequest httpServletRequest = (HttpServletRequest) MessageContext
                    .getCurrentContext().getProperty(
                            HTTPConstants.MC_HTTP_SERVLETREQUEST );

            if ( request == null )
                throw new NullArgumentException(
                        "Cannot submit a null request!" );

            if ( userSubject == null )
                throw new NullArgumentException(
                        "Cannot submit a request for a null user!" );

            if ( caSubject == null )
                throw new NullArgumentException(
                        "Cannot submit a request for a null CA!" );

            String normalizedSubject = DNUtil.normalizeDN( userSubject );
            String normalizedCASubject = DNUtil.normalizeDN( caSubject );

            VOMembershipRequest req = RequestDAO.instance().findPendingForUser(
                    normalizedSubject, normalizedCASubject );

            if ( req != null )
                throw new AlreadyExistsException(
                        "A registration pending for user '"
                                + normalizedSubject
                                + "' has already been received. "
                                + "Follow the instructions that were communicated to complete registration." );

            req = RequestDAO.instance().createFromDNCA( normalizedSubject,
                    normalizedCASubject, request.getEmailAddress() );

            // Notify user
            String confirmURL = buildConfirmURL( httpServletRequest, req );
            String cancelURL = buildCancelURL( httpServletRequest, req );

            ConfirmRequest n = new ConfirmRequest(
                    request.getEmailAddress(), confirmURL, cancelURL );

            try {

                n.send();

            } catch ( VOMSNotificationException e ) {

                log.error( "Error sending notification for request!", e );
                log.error( "Request will be discarded." );
                RequestDAO.instance().delete( req );
            }

        } catch ( RuntimeException e ) {

            ServiceExceptionHelper.handleServiceException( log, e );
            throw e;
        }
    }

}
