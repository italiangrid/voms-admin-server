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

package org.glite.security.voms.admin.view.actions.manager;

import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.GroupManagerDAO;
import org.glite.security.voms.admin.persistence.model.GroupManager;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Result(name=BaseAction.SUCCESS, location="managerIndex")
public class IndexAction extends BaseAction {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	List<GroupManager> groupManagers;
	
	@Override
	public String execute() throws Exception {
	
		GroupManagerDAO dao = DAOFactory.instance().getGroupManagerDAO();
		groupManagers = dao.findAll();
		
		return SUCCESS;
	}

	
	/**
	 * @return the groupManagers
	 */
	public List<GroupManager> getGroupManagers() {
	
		return groupManagers;
	}

	
	/**
	 * @param groupManagers the groupManagers to set
	 */
	public void setGroupManagers(List<GroupManager> groupManagers) {
	
		this.groupManagers = groupManagers;
	}

	
}
