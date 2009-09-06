package org.glite.security.voms.admin.operations.users;

import java.security.cert.X509Certificate;
import java.util.Date;

import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

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
