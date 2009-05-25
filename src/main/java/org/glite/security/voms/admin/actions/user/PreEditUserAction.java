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
package org.glite.security.voms.admin.actions.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actionforms.UserForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.common.VOMSServiceConstants;
import org.glite.security.voms.admin.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.users.FindUnassignedRoles;
import org.glite.security.voms.admin.operations.users.FindUserOperation;




public class PreEditUserAction extends BaseAction {

    private static final Log log = LogFactory.getLog( PreEditUserAction.class );

    public ActionForward execute( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

        UserForm uForm = (UserForm) form;
        log.debug( "userForm: " + uForm );

        VOMSUser user = getUserFromRequest( request );

        if ( user == null )
            user = (VOMSUser) FindUserOperation.instance( uForm.getId() )
                    .execute();
        
        storeUser(request,user);

        Map unassignedRoles = new HashMap();

        Iterator groups = user.getGroups().iterator();

        while ( groups.hasNext() ) {

            VOMSGroup g = (VOMSGroup) groups.next();
            List roles = (List) FindUnassignedRoles.instance( user.getId(),
                    g.getId() ).execute();
            
            unassignedRoles.put( g.getId(), roles );

        }

        request.setAttribute( VOMSServiceConstants.UNASSIGNED_ROLES_KEY, unassignedRoles );

        List unsubscribedGroups = VOMSUserDAO.instance().getUnsubscribedGroups(
                user.getId() );

        request.setAttribute( "unsubscribedGroups", unsubscribedGroups );

        List attributeDescriptions = VOMSAttributeDAO.instance().getAllAttributeDescriptions();
        request.setAttribute("attributeDescriptions", attributeDescriptions);
        
        uForm.reset( mapping, request );

        return findSuccess( mapping );

    }

}
