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

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.actions.search.BaseSearchAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "searchMember.jsp"),
		@Result(name = BaseAction.INPUT, location = "searchMemberError.jsp") })
public class SearchMemberAction extends BaseSearchAction implements
		ModelDriven<VOMSRole>, Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	VOMSGroup group;
	VOMSRole role;

	public void prepare() throws Exception {
		initSearchData(ROLE_MEMBER_SEARCH_NAME);

		group = groupById(searchData.getGroupId());
		role = roleById(searchData.getRoleId());

	}

	public VOMSGroup getGroup() {
		return group;
	}

	public VOMSRole getModel() {

		return role;
	}

}
