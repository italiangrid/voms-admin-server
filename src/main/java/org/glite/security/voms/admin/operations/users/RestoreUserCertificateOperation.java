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

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class RestoreUserCertificateOperation extends BaseVomsOperation {

	VOMSUser user;
	Certificate certificate;

	private RestoreUserCertificateOperation(VOMSUser u, Certificate c) {
		user = u;
		certificate = c;
	}

	public static RestoreUserCertificateOperation instance(VOMSUser u,
			Certificate c) {
		return new RestoreUserCertificateOperation(u, c);
	}

	@Override
	protected Object doExecute() {
		if (user == null)
			throw new NullArgumentException("user cannot be null");

		if (certificate == null)
			throw new NullArgumentException("certificate cannot be null");

		certificate.restore();
		return null;
	}

	@Override
	protected void setupPermissions() {
		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
				.getContainerReadPermission().setMembershipReadPermission()
				.setSuspendPermission());

	}

}
