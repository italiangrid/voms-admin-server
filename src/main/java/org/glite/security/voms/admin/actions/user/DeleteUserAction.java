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
import org.glite.security.voms.admin.actionforms.UserForm;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.operations.users.DeleteUserOperation;


public class DeleteUserAction extends BaseAction {

	private static final Log log = LogFactory.getLog(DeleteUserAction.class);

	public DeleteUserAction() {

		super();
		// TODO Auto-generated constructor stub
	}

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest req,
			HttpServletResponse res) throws Exception {

		ActionMessages msgs = new ActionMessages();
		UserForm uForm = (UserForm) form;

		if (!isCancelled(req)) {
			DeleteUserOperation.instance(uForm.getId()).execute();
			msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"confirm.user.deletion"));
		} else {
			msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"confirm.user.deletion.cancelled"));
		}

		saveMessages(req, msgs);

		return findSuccess(mapping);

	}

}
