/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.view.actions.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.groups.AddMemberOperation;
import org.glite.security.voms.admin.operations.groups.RemoveMemberOperation;
import org.glite.security.voms.admin.operations.users.AssignRoleOperation;
import org.glite.security.voms.admin.operations.users.DismissRoleOperation;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "mappings.jsp"),
		@Result(name = BaseAction.EDIT, location = "userDetail"),
		@Result(name = BaseAction.INPUT, location = "mappings.jsp") })
public class MembershipActions extends UserActionSupport {

	public static final Log log = LogFactory.getLog(MembershipActions.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long groupId;
	Long roleId;

	@Action("add-to-group")
	public String addToGroup() throws Exception {

		// log.debug(String.format("userId: %d, groupId: %d, roleId: %d",
		// userId, groupId, roleId));

		VOMSGroup g = groupById(groupId);

		AddMemberOperation.instance(getModel(), g).execute();

		addActionMessage(getText("confirm.user.add_to_group", new String[] {
				model.getShortName(), g.toString() }));

		return SUCCESS;
	}

	@Action(value = "remove-from-group")
	public String removeFromGroup() throws Exception {

		// log.debug(String.format("userId: %d, groupId: %d, roleId: %d",
		// userId, groupId, roleId));

		VOMSGroup g = groupById(groupId);

		RemoveMemberOperation.instance(getModel(), g).execute();

		addActionMessage(getText("confirm.user.remove_from_group",
				new String[] { model.getShortName(), g.toString() }));

		return SUCCESS;

	}

	@Action(value = "assign-role")
	public String assignRole() throws Exception {

		// log.debug(String.format("userId: %d, groupId: %d, roleId: %d",
		// userId, groupId, roleId));

		VOMSGroup g = groupById(groupId);
		VOMSRole r = roleById(roleId);

		AssignRoleOperation.instance(getModel(), g, r).execute();

		addActionMessage(getText("confirm.user.assign_role", new String[] {
				getModel().getShortName(),r.toString(),g.toString() }));

		return SUCCESS;

	}

	@Action("dismiss-role")
	public String dismissRole() throws Exception {

		VOMSGroup g = groupById(groupId);
		VOMSRole r = roleById(roleId);

		DismissRoleOperation.instance(getModel(), g, r).execute();
		addActionMessage(getText("confirm.user.dismiss_role", new String[] {
				getModel().getShortName(), r.toString(), g.toString() }));

		return SUCCESS;

	}

	@Action("edit")
	public String edit() throws Exception {

		return EDIT;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
