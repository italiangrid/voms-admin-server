package it.infn.cnaf.voms.aa.impl;

import it.infn.cnaf.voms.aa.VOMSAttributeAuthority;
import it.infn.cnaf.voms.aa.VOMSAttributes;

import java.util.List;

import org.glite.security.voms.admin.dao.CertificateDAO;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.database.NoSuchCertificateException;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.database.SuspendedCertificateException;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSUser;

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
			throw new NoSuchCertificateException("Certificate identified by '"
					+ dn + "' " + ((ca != null) ? ",'" + ca + "' " : "")
					+ "not found!");

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
