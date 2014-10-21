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
package org.glite.security.voms.admin.operations.roles;

import org.glite.security.voms.admin.operations.BaseVoReadOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;

public class FindRoleOperation extends BaseVoReadOperation {

	Long id;

	String name;

	private FindRoleOperation(Long id) {

		this.id = id;

	}

	private FindRoleOperation(String name) {
		this.name = name;
	}

	protected Object doExecute() {
		VOMSRoleDAO dao = VOMSRoleDAO.instance();

		if (name != null)
			return dao.findByName(name);

		return dao.findById(id);
	}

	public static FindRoleOperation instance(Long id) {

		return new FindRoleOperation(id);
	}

	public static FindRoleOperation instance(String name) {

		return new FindRoleOperation(name);
	}
}
