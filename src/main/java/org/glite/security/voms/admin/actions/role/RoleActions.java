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

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.glite.security.voms.admin.actionforms.RoleActionsForm;
import org.glite.security.voms.admin.actions.BaseDispatchAction;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.roles.CreateRoleOperation;
import org.glite.security.voms.admin.operations.roles.DeleteRoleOperation;
import org.glite.security.voms.admin.operations.roles.FindRoleOperation;


public class RoleActions extends BaseDispatchAction {

    public ActionForward createRole( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

    	ActionMessages msgs = new ActionMessages();
    	
        RoleActionsForm rForm = (RoleActionsForm) form;

        VOMSRole r = (VOMSRole) CreateRoleOperation.instance( rForm.getName() )
                .execute();

        
        storeRole( request, r );
        
        msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"confirm.role.creation", r.getName()));
        
        saveMessages(request, msgs);

        return findSuccess( mapping );
    }

    public ActionForward deleteRole( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

    	ActionMessages msgs = new ActionMessages();
    	
        RoleActionsForm rForm = (RoleActionsForm) form;

        VOMSRole r = (VOMSRole) DeleteRoleOperation.instance( rForm.getId() ).execute();
        
        msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"confirm.role.deletion", r.getName()));
        
        saveMessages(request, msgs);
        

        return findSuccess( mapping );
    }

    public ActionForward getMembers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception {
    
        RoleActionsForm rForm = (RoleActionsForm) form;
        
        VOMSRole r = (VOMSRole)FindRoleOperation.instance(rForm.getId()).execute();
        VOMSGroup group = (VOMSGroup)FindGroupOperation.instance(rForm.getGroupId()).execute();
        
        storeGroup(request,group);
        
        Set users = r.getUsers(group);
        request.setAttribute("members",users);
        
        return mapping.findForward("toEditPage");
    }
}
