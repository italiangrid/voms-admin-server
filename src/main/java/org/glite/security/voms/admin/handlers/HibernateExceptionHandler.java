/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2008. 
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

import java.sql.BatchUpdateException;

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
import org.glite.security.voms.admin.database.HibernateFactory;
import org.hibernate.JDBCException;


public class HibernateExceptionHandler extends ExceptionHandler {

    private static Log log = LogFactory.getLog(HibernateExceptionHandler.class);
    
    @Override
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
                .info( "Exception caught performing struts action: "
                        + ex.getMessage() );
        log.debug( "Detailed information: ", ex );

        ActionMessage msg = null;
        
        if (ex instanceof JDBCException){
            
            Throwable t = ex.getCause();
            
            log.info( "Caught JDBCException:" + t.getMessage() );
            
            msg = new ActionMessage( "error.database", t.getMessage() );
            
            // Gracefully close the session before control is passed further down the way
            try {
                
                HibernateFactory.rollbackTransaction();
            
            }finally{
                
                HibernateFactory.closeSession();
            }
            
        }else if (ex instanceof BatchUpdateException){
            
            Throwable t = ex.getCause();
            
            log.info( "Caught BatchUpdateException:" + t.getMessage() );
            
            msg = new ActionMessage( "error.database", t.getMessage() );
            
            // Gracefully close the session before control is passed further down the way
            try {
                
                HibernateFactory.rollbackTransaction();
            
            }finally{
                
                HibernateFactory.closeSession();
            }
            
        }else
            log.error("Caught non JDBCException in HibernateExceptionHandler!",ex);
        
        if (msg != null)
            storeException( request, msg.getKey(), msg, forward,
            "request" );
        
        return forward;
    }
}
