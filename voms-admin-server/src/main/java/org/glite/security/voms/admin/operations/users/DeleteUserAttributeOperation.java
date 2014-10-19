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

import org.glite.security.voms.User;
import org.glite.security.voms.admin.operations.BaseAttributeRWOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchUserException;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class DeleteUserAttributeOperation extends BaseAttributeRWOperation {

	VOMSUser user;

	String attributeName;

	private DeleteUserAttributeOperation(VOMSUser u, String aName) {

		super(VOMSContext.getVoContext());

		user = u;
		attributeName = aName;
	}

	public Object doExecute() {

		VOMSUserDAO.instance().deleteAttribute(user, attributeName);
		return null;
	}

	public static DeleteUserAttributeOperation instance(VOMSUser u, String aName) {

		return new DeleteUserAttributeOperation(u, aName);
	}

	public static DeleteUserAttributeOperation instance(User u, String aName) {

		VOMSUser vomsUser = (VOMSUser) FindUserOperation.instance(u.getDN(),
				u.getCA()).execute();

		if (vomsUser == null)
			throw new NoSuchUserException("User '" + u.getDN() + ","
					+ u.getCA() + "' not found in this vo.");

		return new DeleteUserAttributeOperation(vomsUser, aName);
	}
}
