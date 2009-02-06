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
import org.glite.security.voms.admin.actionforms.ConfirmRequestForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.NotFoundException;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.RequestDAO;
import org.glite.security.voms.admin.database.AlreadyConfirmedRequestException;
import org.glite.security.voms.admin.database.ConfirmationRequestException;
import org.glite.security.voms.admin.database.IllegalOperationException;
import org.glite.security.voms.admin.model.VOMembershipRequest;


public class CancelRequestAction extends BaseAction {

    public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        
        ConfirmRequestForm rForm = (ConfirmRequestForm)form;
        
        if (!VOMSConfiguration.instance().getBoolean( VOMSConfiguration.REGISTRATION_SERVICE_ENABLED, true))
            return mapping.findForward( "registrationDisabled" );
        
        Long id = rForm.getRequestId();
        VOMembershipRequest req = RequestDAO.instance().findById( id );
        
        if (req == null)
            throw new NotFoundException("Request with id:"+id+" not found in database!");
        
        if (!req.getStatus().equals(VOMembershipRequest.SUBMITTED))
            throw new AlreadyConfirmedRequestException("Cannot cancel an already confirmed request!");
        
        if (rForm.getConfirmId().equals( req.getConfirmId() ))
            RequestDAO.instance().delete( req );
        else
            throw new ConfirmationRequestException("Wrong confirmation id for request #:"+id);
        
        return findSuccess( mapping );
    }
}
