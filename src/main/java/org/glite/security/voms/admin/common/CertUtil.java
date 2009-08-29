package org.glite.security.voms.admin.common;

import java.io.InputStream;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CertUtil {

	static {
		if (Security.getProvider("BC") == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	public static void validateCertificate(X509Certificate cert) {

	}

	public static X509Certificate parseCertficate(InputStream in)
			throws CertificateException {

		CertificateFactory certFactory = CertificateFactory.getInstance(
				"X.509", Security.getProvider("BC"));

		X509Certificate cert = (X509Certificate) certFactory
				.generateCertificate(in);

		validateCertificate(cert);
		return cert;

	}

}
