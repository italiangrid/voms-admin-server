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
package org.glite.security.voms.admin.actions.group;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.GroupAttributesActionsForm;
import org.glite.security.voms.admin.actions.BaseDispatchAction;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.operations.groups.DeleteGroupAttributeOperation;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.groups.SetGroupAttributeOperation;



public class GroupAttributesActions extends BaseDispatchAction {

    private static final Log log = LogFactory.getLog(GroupAttributesActions.class);
    
    public ActionForward set( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        GroupAttributesActionsForm gForm = (GroupAttributesActionsForm)form;
        
        log.debug(gForm);
        
        VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(gForm.getGroupId()).execute();
        
        SetGroupAttributeOperation.instance(g,gForm.getAttributeName(),gForm.getAttributeDescription(),gForm.getAttributeValue()).execute();
                
        return findSuccess(mapping);
    }
    
    public ActionForward delete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        GroupAttributesActionsForm gForm = (GroupAttributesActionsForm)form;
        
        log.debug(gForm);
        
        VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(gForm.getGroupId()).execute();
        
        DeleteGroupAttributeOperation.instance(g,gForm.getAttributeName()).execute();
        
        return findSuccess(mapping);
    }

}
