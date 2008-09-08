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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.GroupActionsForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.VOMSAuthorizationException;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.SearchResults;
import org.glite.security.voms.admin.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.groups.SearchMembersOperation;




public class LoadGroupAction extends BaseAction {

    public static final Log log = LogFactory.getLog( LoadGroupAction.class );

    public ActionForward execute( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {
        
        GroupActionsForm gForm =  (GroupActionsForm)form;
        
        log.debug("gForm:"+gForm);
        VOMSGroup g = getGroupFromRequest(request);
        log.debug("group:"+g);
        
        if (g == null){
                    
            g = (VOMSGroup)FindGroupOperation.instance(gForm.getId()).execute();
            storeGroup(request,g);
        }
        
        // Get all members here
        int maxResults = VOMSConfiguration.instance().getInt(VOMSConfiguration.USER_MAX_RESULTS_PER_PAGE,15);
        SearchResults results = getResultsFromRequest(request);
        
        if (results == null){
        
        	try{
        		
        		results = (SearchResults) SearchMembersOperation.instance(g,null,0,maxResults).execute();
        	
        	}catch(VOMSAuthorizationException e){
        		
        		// Do nothing, just don't store group members in the request context
        		
        	}
        
        }
        
        List attributeDescriptions = VOMSAttributeDAO.instance().getAllAttributeDescriptions();
        request.setAttribute("attributeDescriptions", attributeDescriptions);
        
        storeResults(request,results);
        
        return findSuccess( mapping );
    }
}
