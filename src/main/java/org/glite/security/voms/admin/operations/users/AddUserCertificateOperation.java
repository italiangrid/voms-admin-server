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

import java.security.cert.X509Certificate;
import java.util.Date;

import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class AddUserCertificateOperation extends BaseVomsOperation {

	VOMSUser theUser;
	X509Certificate theCert;

	String subject;
	String issuer;

	Date notAfter;
	


	private AddUserCertificateOperation(VOMSUser u, X509Certificate cert) {

		if (u == null)
			throw new NullArgumentException("User cannot be null!");

		if (cert == null)
			throw new NullArgumentException("cert cannot be null!");

		theUser = u;
		theCert = cert;

	}

	private AddUserCertificateOperation(VOMSUser u, String certSubject,
			String certIssuer, String certNotAfter) {

		if (u == null)
			throw new NullArgumentException("User cannot be null!");

		if (certSubject == null)
			throw new NullArgumentException("certSubject cannot be null!");

		if (certIssuer == null)
			throw new NullArgumentException("certIssuer cannot be null!");

		theUser = u;
		subject = certSubject;
		issuer = certIssuer;

	}

	public static AddUserCertificateOperation instance(VOMSUser u,
			X509Certificate cert) {

		return new AddUserCertificateOperation(u, cert);
	}

	public static AddUserCertificateOperation instance(VOMSUser u,
			String certSubject, String certIssuer, String certNotAfter) {

		return new AddUserCertificateOperation(u, certSubject, certIssuer,
				certNotAfter);
	}

	
	
	protected Object doExecute() {

		if (theCert != null)
			VOMSUserDAO.instance().addCertificate(theUser, theCert);
		else
			VOMSUserDAO.instance().addCertificate(theUser, subject, issuer);

		return null;
	}

	protected void setupPermissions() {

		addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
				.getContainerRWPermissions().setMembershipRWPermission());

	}

}
