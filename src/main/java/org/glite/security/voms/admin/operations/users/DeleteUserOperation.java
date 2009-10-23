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
package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVoRWOperation;

public class DeleteUserOperation extends BaseVoRWOperation {

	VOMSUser usr = null;

	Long id;

	private DeleteUserOperation(VOMSUser u) {

		this.usr = u;
	}

	private DeleteUserOperation(Long id) {

		this.id = id;
	}

	public Object doExecute() {

		if (usr == null)
			VOMSUserDAO.instance().delete(id);
		else
			VOMSUserDAO.instance().delete(usr);

		return null;
	}

	public static DeleteUserOperation instance(VOMSUser u) {

		return new DeleteUserOperation(u);
	}

	public static DeleteUserOperation instance(Long id) {

		return new DeleteUserOperation(id);
	}

	public static DeleteUserOperation instance(String username, String userCa) {

		VOMSUser u = (VOMSUser) FindUserOperation.instance(username, userCa)
				.execute();
		if (u == null)
			throw new NoSuchUserException("User '" + username + "," + userCa
					+ "' not found in org.glite.security.voms.admin.database!");
		return new DeleteUserOperation(u);
	}
}
