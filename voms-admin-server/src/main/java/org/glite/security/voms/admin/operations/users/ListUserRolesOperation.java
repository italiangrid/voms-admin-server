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

import org.glite.security.voms.admin.operations.BaseVoReadOperation;
import org.glite.security.voms.admin.persistence.error.NoSuchUserException;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class ListUserRolesOperation extends BaseVoReadOperation {

	String username, caDN;

	public ListUserRolesOperation(String username, String caDN) {

		this.username = username;
		this.caDN = caDN;
	}

	protected Object doExecute() {

		VOMSUser u = (VOMSUser) FindUserOperation.instance(username, caDN)
				.execute();

		if (u == null)
			throw new NoSuchUserException("No user '" + username + "," + caDN
					+ "' found in org.glite.security.voms.admin.persistence.error.");

		return u.getRoleMappings();
	}

	public static ListUserRolesOperation instance(String username, String caDN) {

		return new ListUserRolesOperation(username, caDN);
	}
}
