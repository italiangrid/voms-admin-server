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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.glite.security.voms.admin.actionforms.MembershipActionsForm;
import org.glite.security.voms.admin.actions.BaseDispatchAction;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.groups.AddMemberOperation;
import org.glite.security.voms.admin.operations.groups.RemoveMemberOperation;
import org.glite.security.voms.admin.operations.users.AssignRoleOperation;
import org.glite.security.voms.admin.operations.users.DismissRoleOperation;


public class MembershipActions extends BaseDispatchAction {

	private static final Log log = LogFactory.getLog(MembershipActions.class);

	public ActionForward addToGroup(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages msgs = new ActionMessages();

		MembershipActionsForm mForm = (MembershipActionsForm) form;

		log.debug("mForm: " + mForm);

		VOMSUser u = VOMSUserDAO.instance().findById(mForm.getUserId());
		VOMSGroup g = VOMSGroupDAO.instance().findById(mForm.getGroupId());
        
        storeUser(request, u);
        storeGroup(request, g);

		AddMemberOperation.instance(u, g).execute();

		
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"confirm.user.add_to_group", u, g.getName()));

		saveMessages(request, msgs);
		return findSuccess(mapping);
	}

	public ActionForward removeFromGroup(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages msgs = new ActionMessages();

		MembershipActionsForm mForm = (MembershipActionsForm) form;

		log.debug("mForm: " + mForm);

		VOMSUser u = VOMSUserDAO.instance().findById(mForm.getUserId());
		VOMSGroup g = VOMSGroupDAO.instance().findById(mForm.getGroupId());
        
        storeUser(request, u);
        storeGroup(request, g);

		RemoveMemberOperation.instance(u, g).execute();

		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"confirm.user.remove_from_group", u, g.getName()));

		saveMessages(request, msgs);
		return findSuccess(mapping);
	}

	public ActionForward assignRole(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MembershipActionsForm mForm = (MembershipActionsForm) form;

		log.debug("mForm: " + mForm);

		VOMSUser u = VOMSUserDAO.instance().findById(mForm.getUserId());
		VOMSGroup g = VOMSGroupDAO.instance().findById(mForm.getGroupId());
		VOMSRole r = VOMSRoleDAO.instance().findById(mForm.getRoleId());

        
        storeUser(request, u);
        storeGroup(request, g);
        storeRole(request, r);
        
		AssignRoleOperation.instance(u, g, r).execute();	

		ActionMessages msgs = new ActionMessages();
		msgs.add(ActionMessages.GLOBAL_MESSAGE,
				new ActionMessage("confirm.user.assign_role", u, r
						.getName(), g.getName()));
		saveMessages(request, msgs);

		return findSuccess(mapping);
	}

	public ActionForward dismissRole(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MembershipActionsForm mForm = (MembershipActionsForm) form;

		log.debug("mForm: " + mForm);

		VOMSUser u = VOMSUserDAO.instance().findById(mForm.getUserId());
		VOMSGroup g = VOMSGroupDAO.instance().findById(mForm.getGroupId());
		VOMSRole r = VOMSRoleDAO.instance().findById(mForm.getRoleId());

        storeUser(request, u);
        storeGroup(request, g);
        storeRole(request, r);
       
		DismissRoleOperation.instance(u, g, r).execute();
		
		ActionMessages msgs = new ActionMessages();
		msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"confirm.user.dismiss_role", u, r.getName(), g
						.getName()));
		saveMessages(request, msgs);

		return findSuccess(mapping);
	}

}
