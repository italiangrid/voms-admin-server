package org.glite.security.voms.admin.dao;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.common.DNUtil;
import org.glite.security.voms.admin.database.HibernateFactory;
import org.glite.security.voms.admin.database.NoSuchCAException;
import org.glite.security.voms.admin.model.Certificate;
import org.glite.security.voms.admin.model.VOMSCA;
import org.glite.security.voms.admin.model.VOMSUser;

public class CertificateDAO {

	private CertificateDAO() {
		HibernateFactory.beginTransaction();
	}

	public Certificate findByDNCA(String dn, String ca) {

		assert dn != null : "Null DN passed as argument!";
		assert ca != null : "Null ca passed as argument!";

		String query = "From Certificate where subjectString = :subjectString and ca.subjectString = :ca";

		Certificate dbCert = (Certificate) HibernateFactory.getSession()
				.createQuery(query).setString("subjectString", dn).setString(
						"ca", ca).uniqueResult();

		return dbCert;
	}

	public Certificate findById(long id) {

		return (Certificate) HibernateFactory.getSession().get(
				Certificate.class, new Long(id));
	}

	public Certificate find(X509Certificate cert) {

		assert cert != null : "Null certificate passed as argument!";

		String subjectString = DNUtil.getBCasX500(cert
				.getSubjectX500Principal());

		String query = "from Certificate where subjectString = :subjectString";

		Certificate dbCert = (Certificate) HibernateFactory.getSession()
				.createQuery(query).setString("subjectString", subjectString)
				.uniqueResult();

		return dbCert;

	}

	public boolean isAlreadyAssigned(X509Certificate cert) {

		Certificate dbCert = find(cert);
		return (dbCert != null);
	}


	public Certificate create(VOMSUser u, String ca) {

		assert u != null : "null user passed as argument!";
		assert ca != null : "null ca passed as argument!";

		String normalizedCA = DNUtil.normalizeDN(ca);

		VOMSCA dbCA = VOMSCADAO.instance().getByName(normalizedCA);

		if (dbCA == null)
			throw new NoSuchCAException("CA '" + ca
					+ "' not found in database!");
		Certificate cert = new Certificate();

		cert.setSubjectString(DNUtil.normalizeDN(u.getDn()));
		cert.setCa(dbCA);
		cert.setCreationTime(new Date());
		cert.setUser(u);

		return cert;
	}

	public List<Certificate> getForCA(VOMSCA ca) {
		assert ca != null : "null ca passed as argument!";

		String query = "from Certificate where ca = :ca";
		return HibernateFactory.getSession().createQuery(query).setEntity("ca",
				ca).list();
	}

	public List<Certificate> getAll() {

		String query = "from Certificate";
		return HibernateFactory.getSession().createQuery(query).list();

	}

	public static CertificateDAO instance() {
		return new CertificateDAO();
	}
}
