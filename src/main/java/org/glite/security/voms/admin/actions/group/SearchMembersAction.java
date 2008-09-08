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
import org.glite.security.voms.admin.actionforms.MembershipSearchForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.SearchResults;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.groups.SearchMembersOperation;
import org.glite.security.voms.admin.operations.roles.FindRoleOperation;




public class SearchMembersAction extends BaseAction {

    private static final Log log = LogFactory
            .getLog( SearchMembersAction.class );

    public ActionForward execute( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

        MembershipSearchForm sForm = (MembershipSearchForm) form;

        log.debug( sForm );

        VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(
                sForm.getGroupId() ).execute();
        
        VOMSRole r = null;

        if ( sForm.getRoleId() != null ) {
            r = (VOMSRole) FindRoleOperation.instance( sForm.getRoleId() )
                    .execute();

        }

        int maxResults = VOMSConfiguration.instance().getInt(
                VOMSConfiguration.USER_MAX_RESULTS_PER_PAGE, 15 );

        SearchResults results = (SearchResults) SearchMembersOperation
                .instance( g, r, sForm.getText(), sForm.getFirstResults(),
                        maxResults ).execute();

        log.debug( "results: " + results );

        storeGroup( request, g );
        storeResults( request, results );

        sForm.reset( mapping, request );

        return findSuccess( mapping );
    }

}
