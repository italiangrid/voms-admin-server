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
package org.glite.security.voms.admin.view.actions.role;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.operations.roles.CreateRoleOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "/role/search.action", type = "redirect"),
		@Result(name = BaseAction.INPUT, location = "roleCreate") })
@Action(value = "create", interceptorRefs = { @InterceptorRef(value = "authenticatedStack", params = {
		"tokenSession.includeMethods", "execute" }) })
public class CreateAction extends RoleActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	VOMSRole role;
	String roleName;

	public VOMSRole getModel() {

		return role;
	}

	public void prepare() throws Exception {

	}

	public String execute() throws Exception {

		VOMSRole r = (VOMSRole) CreateRoleOperation.instance(getRoleName())
				.execute();
		if (r != null)
			addActionMessage(getText("confirm.role.creation", r.getName()));

		return SUCCESS;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "A name for the role is required!")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The role name field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Override
	public void validate() {
		
		VOMSRole r = VOMSRoleDAO.instance().findByName(roleName);
		if (r != null)
			addFieldError("roleName", "Role '"+roleName+"' already exists!");
		
	}

}
