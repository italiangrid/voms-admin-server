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
import org.glite.security.voms.admin.common.VOMSAuthorizationException;
import org.glite.security.voms.admin.database.VOMSDatabaseException;



public class GeneralExceptionHandler extends ExceptionHandler {

    private static Log log = LogFactory.getLog( GeneralExceptionHandler.class );

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

        log.error( "Caught exception in GeneralExceptionHandler: ", ex );
        log.error("Message: "+ex.getMessage());
        log.error("Cause: "+ex.getCause());
        
        ActionMessage msg = null;

        if ( ex instanceof VOMSAuthorizationException ) {

            VOMSAuthorizationException e = (VOMSAuthorizationException) ex;
            msg = new ActionMessage( "error.authorization_failed", e
                    .getOperation().getName() );

        } else if ( ex instanceof VOMSDatabaseException ) {

            msg = new ActionMessage( "error.database", ex.getMessage() );

        } else if (ex instanceof ServletException){
            
            msg = new ActionMessage("error.servlet",ex.getMessage());
        
        }else {

            msg = new ActionMessage( "error.unknown", ex.getMessage() );

        }

        storeException( request, msg.getKey(), msg, forward, "org.glite.security.voms.admin.request" );

        return forward;

    }

}
