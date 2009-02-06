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
package org.glite.security.voms.admin.handlers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.common.VOMSAuthorizationException;
import org.glite.security.voms.admin.database.AlreadyMemberException;
import org.glite.security.voms.admin.database.AttributeAlreadyExistsException;
import org.glite.security.voms.admin.database.EmailAddressAlreadyBoundException;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.database.NoSuchAttributeException;
import org.glite.security.voms.admin.database.NoSuchCAException;
import org.glite.security.voms.admin.database.NoSuchGroupException;
import org.glite.security.voms.admin.database.NoSuchMappingException;
import org.glite.security.voms.admin.database.NoSuchRoleException;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.database.UserAlreadyExistsException;
import org.glite.security.voms.admin.database.VOMSDatabaseException;

public class UserActionsExceptionHandler extends ExceptionHandler {

    private static final Log log = LogFactory
            .getLog( UserActionsExceptionHandler.class );

    public ActionForward execute( Exception ex, ExceptionConfig ae,
            ActionMapping mapping, ActionForm formInstance,
            HttpServletRequest request, HttpServletResponse response )
            throws ServletException {

        ActionForward forward = null;

        if ( ae.getPath() != null ) {
            forward = new ActionForward( ae.getPath() );
        } else {
            forward = mapping.getInputForward();

        }

        log
                .info( "Exception caught performing user action: "
                        + ex.getMessage() );
        log.debug( "Detailed information: ", ex );

        ActionMessage msg = null;

        if ( ex instanceof VOMSAuthorizationException ) {

            msg = new ActionMessage( "error.authorization_failed", ex
                    .getMessage() );

        } else if ( ex instanceof NullArgumentException ) {

            msg = new ActionMessage( "error.null_argument", ex.getMessage() );

        } else if ( ex instanceof EmailAddressAlreadyBoundException ) {

            msg = new ActionMessage( "error.user.email_address_already_bound",
                    ex );

        } else if ( ex instanceof UserAlreadyExistsException ) {

            msg = new ActionMessage( "error.user.already_exists", ex
                    .getMessage() );

        } else if ( ex instanceof NoSuchUserException ) {

            msg = new ActionMessage( "error.user.not_found", ex.getMessage() );

        } else if ( ex instanceof NullArgumentException ) {

            msg = new ActionMessage( "error.null_argument", ex.getMessage() );

        } else if ( ex instanceof NoSuchCAException ) {

            msg = new ActionMessage( "error.ca.not_found", ex.getMessage() );

        } else if ( ex instanceof AttributeAlreadyExistsException ) {
            msg = new ActionMessage( "error.attribute.already_exists", ex
                    .getMessage() );

        } else if ( ex instanceof NoSuchAttributeException ) {
            msg = new ActionMessage( "error.attribute.not_found", ex
                    .getMessage() );

        } else if ( ex instanceof NoSuchGroupException ) {
            msg = new ActionMessage( "error.group.not_found", ex.getMessage() );

        } else if ( ex instanceof NoSuchRoleException ) {

            msg = new ActionMessage( "error.role.not_found", ex.getMessage() );

        } else if ( ex instanceof AlreadyMemberException ) {

            msg = new ActionMessage( "error.user.already_member", ex
                    .getMessage() );

        } else if ( ex instanceof NoSuchMappingException ) {

            msg = new ActionMessage( "error.mapping.not_found", ex.getMessage() );

        } else if ( ex instanceof VOMSDatabaseException ) {

            msg = new ActionMessage( "error.database", ex.getMessage() );

            
        } else {
            msg = new ActionMessage( "error.unknown", ex.getMessage() );

        }

        storeException( request, msg.getKey(), msg, forward,
                "request" );

        return forward;

    }

}
