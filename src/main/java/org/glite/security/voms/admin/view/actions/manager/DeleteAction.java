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

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.group_manager.DeleteGroupManagerOperation;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.GroupManagerDAO;
import org.glite.security.voms.admin.persistence.model.GroupManager;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;

@Results({
		@Result(name=BaseAction.SUCCESS, location="index", type="redirectAction")
})

public class DeleteAction extends BaseAction implements Preparable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long id;
	GroupManager manager;

	
	@Override
	public void prepare() throws Exception {

		GroupManagerDAO dao = DAOFactory.instance().getGroupManagerDAO();
		manager = dao.findById(id, true);
	}

	@Override
	public String execute() throws Exception {
		
		if (manager != null){
			DeleteGroupManagerOperation op = new DeleteGroupManagerOperation(manager);
			op.execute();
		}
				
		addActionMessage("Manager succesfully deleted: "+manager.getName());
		return SUCCESS;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
	
		return id;
	}

	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
	
		this.id = id;
	}

	

}
