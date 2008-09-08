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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.glite.security.voms.admin.actionforms.GroupActionsForm;
import org.glite.security.voms.admin.actions.BaseDispatchAction;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.operations.groups.CreateGroupOperation;
import org.glite.security.voms.admin.operations.groups.DeleteGroupOperation;


public class GroupActions extends BaseDispatchAction {

    public ActionForward createGroup( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

    	ActionMessages msgs = new ActionMessages();
    	
        GroupActionsForm gForm = (GroupActionsForm) form;

        String name = gForm.getParentGroupName()+"/"+gForm.getName();
        
        VOMSGroup g = (VOMSGroup) CreateGroupOperation.instance(
                name ).execute();

        msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"confirm.group.creation", g.getName()));
        
        saveMessages(request, msgs);
        
        storeGroup( request, g );

        return findSuccess( mapping );
    }

    public ActionForward deleteGroup( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws Exception {

    	ActionMessages msgs = new ActionMessages();
    	
        GroupActionsForm gForm = (GroupActionsForm) form;

        if (!isCancelled(request)){
            VOMSGroup g = (VOMSGroup) DeleteGroupOperation.instance( gForm.getId() ).execute();
            
            msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
    				"confirm.group.deletion", g.getName()));
            
            saveMessages(request, msgs);
            
        }
        
        

        return findSuccess( mapping );
    }

}
