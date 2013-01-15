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

package org.glite.security.voms.admin.view.actions.apiv2;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.apiv2.JSONSerializer;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.operations.users.ListSuspendedUsersOperation;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("json")
@Results( { @Result(name = BaseAction.SUCCESS, type = "json") })
public class SuspendedUsersAction extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	List<VOMSUserJSON> suspendedUsers;
	
	@Override
	public String execute() throws Exception {
		
		suspendedUsers = JSONSerializer.serialize((List<VOMSUser>)ListSuspendedUsersOperation.instance().execute());
		
		return BaseAction.SUCCESS;
	}

	public List<VOMSUserJSON> getSuspendedUsers() {
		return suspendedUsers;
	}
		
}