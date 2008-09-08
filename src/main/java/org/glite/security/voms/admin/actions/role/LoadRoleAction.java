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
package org.glite.security.voms.admin.actions.role;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.RoleActionsForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.SearchResults;
import org.glite.security.voms.admin.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.operations.groups.SearchMembersOperation;
import org.glite.security.voms.admin.operations.roles.FindRoleOperation;


public class LoadRoleAction extends BaseAction {

    private static final Log log = LogFactory.getLog( LoadRoleAction.class );
    
    public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    

        RoleActionsForm rForm = (RoleActionsForm)form;
        log.debug(rForm);
        
        VOMSRole r = getRoleFromRequest(request);
        
        if (r == null){
            
            r = (VOMSRole) FindRoleOperation.instance(rForm.getId()).execute();
            storeRole(request,r);
        }
        
        List allGroups = (List) ListGroupsOperation.instance().execute(); 
        
        request.setAttribute("allGroups",allGroups);
        
        VOMSGroup g = getGroupFromRequest(request);
        
        if (g == null){
            g = VOMSGroupDAO.instance().getVOGroup();
            storeGroup(request,g);
        }
        
        int maxResults = VOMSConfiguration.instance().getInt(VOMSConfiguration.USER_MAX_RESULTS_PER_PAGE,15);
        SearchResults results = getResultsFromRequest(request);
        
        if (results == null)
            results = (SearchResults) SearchMembersOperation.instance(g,r,null,0,maxResults).execute();
        
        storeResults(request,results);
            
        List attributeDescriptions = VOMSAttributeDAO.instance().getAllAttributeDescriptions();
        request.setAttribute("attributeDescriptions", attributeDescriptions);
        
        return findSuccess(mapping);
    }

}
