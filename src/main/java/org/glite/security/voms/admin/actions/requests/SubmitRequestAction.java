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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.SubmitRequestActionForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.common.VOMSConfigurationException;
import org.glite.security.voms.admin.common.VOMSException;
import org.glite.security.voms.admin.dao.RequestDAO;
import org.glite.security.voms.admin.model.VOMembershipRequest;
import org.glite.security.voms.admin.notification.ConfirmRequestNotification;
import org.glite.security.voms.admin.request.VOMSNotificationException;


public class SubmitRequestAction extends BaseAction {

    private String buildConfirmURL(HttpServletRequest request, VOMembershipRequest membReq){
        
         return getBaseContext( request )+"/ConfirmVOMembershipRequest.do?requestId="+membReq.getId()+"&confirmId="+membReq.getConfirmId(); 
    }
    
    private String buildCancelURL(HttpServletRequest request, VOMembershipRequest membReq){
        
        return getBaseContext( request )+"/CancelVOMembershipRequest.do?requestId="+membReq.getId()+"&confirmId="+membReq.getConfirmId(); 
   }
    
    public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        SubmitRequestActionForm sForm = (SubmitRequestActionForm)form;
        
        String emailAddress = sForm.getEmailAddress();
        
        VOMembershipRequest req = RequestDAO.instance().findFromAdmin();
        
        if (!VOMSConfiguration.instance().getBoolean( VOMSConfiguration.REGISTRATION_SERVICE_ENABLED, true))
            return mapping.findForward( "registrationDisabled" );
        
        if (req == null)
            req = RequestDAO.instance().createFromAdmin( emailAddress );
        
        else
            return findSuccess( mapping );
            
        
        
        // Notify user
        String confirmURL = buildConfirmURL( request, req );
        String cancelURL = buildCancelURL( request, req );
        ConfirmRequestNotification n = new ConfirmRequestNotification(emailAddress,confirmURL,cancelURL);
        
        try{
            
            n.send();
            
        }catch (VOMSNotificationException e) {
        
            log.error( "Error sending notification for request!", e  );
            log.error( "Request will be discarded." );
            RequestDAO.instance().delete( req );
        }
        
        return findSuccess( mapping );
    }
}
