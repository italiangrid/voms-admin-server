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

import org.glite.security.voms.admin.operations.BaseUserAdministrativeOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;

public class FindUserOperation extends BaseUserAdministrativeOperation {

	Long id;

	String dn = null;

	String caDN = null;

	String emailAddress = null;

	private FindUserOperation(Long userId) {

		id = userId;
		setAuthorizedUser(VOMSUserDAO.instance().findById(userId));
		
		
	}

	private FindUserOperation(String dn, String ca) {

		this.dn = dn;
		this.caDN = ca;
		
		setAuthorizedUser(VOMSUserDAO.instance().findByDNandCA(dn, caDN));
		
	}

	private FindUserOperation(String emailAddress) {

		this.emailAddress = emailAddress;
	}

	protected Object doExecute() {

		if (dn == null)

			return VOMSUserDAO.instance().findById(id);

		else if (emailAddress == null)

			return VOMSUserDAO.instance().findByDNandCA(dn, caDN);

		else
			return VOMSUserDAO.instance().findByEmail(emailAddress);

	}

	public static FindUserOperation instance(Long id) {

		return new FindUserOperation(id);
	}

	public static FindUserOperation instance(String dn, String caDN) {

		return new FindUserOperation(dn, caDN);
	}

	public static FindUserOperation instance(String emailAddress) {

		return new FindUserOperation(emailAddress);
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission.getContainerReadPermission().setMembershipReadPermission());
		
	}

}
