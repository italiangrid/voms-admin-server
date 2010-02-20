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

package it.infn.cnaf.voms.aa.impl;

import it.infn.cnaf.voms.aa.VOMSAttributeAuthority;
import it.infn.cnaf.voms.aa.VOMSAttributes;

import java.util.List;

import org.glite.security.voms.admin.persistence.dao.CertificateDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchCertificateException;
import org.glite.security.voms.admin.persistence.error.NoSuchUserException;
import org.glite.security.voms.admin.persistence.error.SuspendedCertificateException;
import org.glite.security.voms.admin.persistence.error.SuspendedUserException;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

/**
 * 
 * @author Andrea Ceccanti
 * 
 */
public class VOMSAAImpl implements VOMSAttributeAuthority {

	protected void checkCertificateValidity(String dn, String ca) {

		assert dn != null : "Please specify a DN for the certificate!";

		CertificateDAO dao = CertificateDAO.instance();
		Certificate cert = null;

		if (ca == null)
			cert = dao.findByDN(dn);
		else
			cert = dao.findByDNCA(dn, ca);

		if (cert == null)
			throw new NoSuchCertificateException("User identified by '"
					+ dn + "' " + ((ca != null) ? ",'" + ca + "' " : "")
					+ "not found!");

		VOMSUser user = cert.getUser();
		
		if (user.isSuspended())
			throw new SuspendedUserException("User identified by '"
					+ dn + "' " + ((ca != null) ? ",'" + ca + "' " : "")
					+ "is currently suspended!");
		
		if (cert.isSuspended())
			throw new SuspendedCertificateException("Certificate '"
					+ cert.getSubjectString() + ", "
					+ cert.getCa().getSubjectString()
					+ "' is currently suspended for the following reason: "
					+ cert.getSuspensionReason());

	}

	public VOMSAttributes getAllVOMSAttributes(String dn) {

		checkCertificateValidity(dn, null);
		VOMSUser u = VOMSUserDAO.instance().findBySubject(dn);

		if (u == null)
			throw new NoSuchUserException("User '" + dn
					+ "' not found in database!");

		return VOMSAttributesImpl.getAllFromUser(u);

	}

	public VOMSAttributes getAllVOMSAttributes(String dn, String ca) {
		checkCertificateValidity(dn, ca);
		VOMSUser u = VOMSUserDAO.instance().findByDNandCA(dn, ca);
		if (u == null)
			throw new NoSuchUserException("User '" + dn + ",'" + ca
					+ "' not found in database!");

		return VOMSAttributesImpl.getAllFromUser(u);
	}

	public VOMSAttributes getVOMSAttributes(String dn) {

		checkCertificateValidity(dn, null);

		VOMSUser u = VOMSUserDAO.instance().findBySubject(dn);

		if (u == null)
			throw new NoSuchUserException("User '" + dn
					+ "' not found in database!");

		return VOMSAttributesImpl.fromUser(u);

	}

	public VOMSAttributes getVOMSAttributes(String dn,
			List<String> requestedFQANs) {

		checkCertificateValidity(dn, null);

		VOMSUser u = VOMSUserDAO.instance().findBySubject(dn);

		if (u == null)
			throw new NoSuchUserException("User '" + dn
					+ "' not found in database!");

		return VOMSAttributesImpl.fromUser(u, requestedFQANs);

	}

	public VOMSAttributes getVOMSAttributes(String dn, String ca) {

		checkCertificateValidity(dn, ca);
		VOMSUser u = VOMSUserDAO.instance().findByDNandCA(dn, ca);

		if (u == null)
			throw new NoSuchUserException("User '" + dn + ",'" + ca
					+ "' not found in database!");

		return VOMSAttributesImpl.fromUser(u);
	}

	public VOMSAttributes getVOMSAttributes(String dn, String ca,
			List<String> requestedFQANs) {

		checkCertificateValidity(dn, ca);

		VOMSUser u = VOMSUserDAO.instance().findByDNandCA(dn, ca);
		if (u == null)
			throw new NoSuchUserException("User '" + dn + ",'" + ca
					+ "' not found in database!");

		return VOMSAttributesImpl.fromUser(u, requestedFQANs);

	}

}
