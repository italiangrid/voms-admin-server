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
package org.glite.security.voms.admin.view.actions.group;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.operations.groups.CreateGroupOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "/group/search.action", type = "redirect"),
		@Result(name = BaseAction.INPUT, location = "groupCreate") })
@InterceptorRef(value = "authenticatedStack", params = {
		"tokenSession.includeMethods", "execute" })
public class CreateAction extends GroupActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	VOMSGroup group;

	String groupName;
	String parentGroupName;

	@Override
	public void validate() {
		
		String candidateName = getParentGroupName()+"/"+getGroupName();
		
		VOMSGroup target = VOMSGroupDAO.instance().findByName(candidateName);
		
		if (target != null){
			
			addFieldError("groupName", "Group '"+candidateName+"' already exists!");
		}
			
	}
	@Override
	public String execute() throws Exception {

		if (groupName == null && parentGroupName == null)
			return INPUT;

		String name = getParentGroupName() + "/" + getGroupName();

		VOMSGroup g = (VOMSGroup) CreateGroupOperation.instance(name).execute();

		if (g != null)
			addActionMessage(getText("confirm.group.creation", g.getName()));

		return SUCCESS;

	}

	public VOMSGroup getModel() {

		return group;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "A name for the group is required!")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The group name field contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getParentGroupName() {
		return parentGroupName;
	}

	public void setParentGroupName(String parentGroupName) {
		this.parentGroupName = parentGroupName;
	}

	@Override
	public void prepare() throws Exception {

	}

}
