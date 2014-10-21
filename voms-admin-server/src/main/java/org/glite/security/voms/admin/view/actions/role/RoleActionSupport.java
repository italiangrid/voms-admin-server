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

import java.util.List;

import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.persistence.model.VOMSAttributeDescription;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class RoleActionSupport extends BaseAction implements
		ModelDriven<VOMSRole>, Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long roleId = -1L;

	VOMSRole role;

	List<VOMSAttributeDescription> attributeClasses;
	List<VOMSGroup> voGroups;

	public VOMSRole getModel() {
		return role;
	}

	public void prepare() throws Exception {

		if (getModel() == null) {

			if (roleId != -1)
				role = roleById(roleId);
		}

		attributeClasses = (List<VOMSAttributeDescription>) VOMSAttributeDAO
				.instance().getAllAttributeDescriptions();
		voGroups = (List<VOMSGroup>) ListGroupsOperation.instance().execute();
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<VOMSGroup> getVoGroups() {
		return voGroups;
	}

	public void setVoGroups(List<VOMSGroup> voGroups) {
		this.voGroups = voGroups;
	}

	public List<VOMSAttributeDescription> getAttributeClasses() {
		return attributeClasses;
	}

}
