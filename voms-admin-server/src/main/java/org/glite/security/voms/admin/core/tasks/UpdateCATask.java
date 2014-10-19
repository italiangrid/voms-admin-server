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
package org.glite.security.voms.admin.core.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.cert.X509Certificate;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.persistence.dao.VOMSCADAO;
import org.glite.security.voms.admin.util.DNUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.impl.CertificateUtils;
import eu.emi.security.authn.x509.impl.CertificateUtils.Encoding;

/**
 * @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a> 
 */
public final class UpdateCATask implements Runnable {

	static final Logger log = LoggerFactory.getLogger(UpdateCATask.class);

	private void directorySanityChecks(File directory) {
		if (!directory.exists())
			throw new VOMSException("Local trust directory does not exists:"
					+ directory.getAbsolutePath());

		if (!directory.isDirectory())
			throw new VOMSException("Local trust directory is not a directory:"
					+ directory.getAbsolutePath());

		if (!directory.canRead())
			throw new VOMSException("Local trust directory is not readable:"
					+ directory.getAbsolutePath());

		if (!directory.canExecute())
			throw new VOMSException("Local trust directory is not traversable:"
					+ directory.getAbsolutePath());
	}

	public void run() {

		String trustAnchorsDir = VOMSConfiguration.instance().getString(
				VOMSConfigurationConstants.TRUST_ANCHORS_DIR,
				"/etc/grid-security/certificates");

		log.debug("Updating CAs from: " + trustAnchorsDir);

		VOMSCADAO dao = VOMSCADAO.instance();

		File dir = new File(trustAnchorsDir);
		directorySanityChecks(dir);

		File[] certFiles = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches(".*\\.\\d");
			}
		});

		for (File caFile : certFiles) {
			log.debug("Parsing CA certificate from {}.", caFile);

			X509Certificate caCert;

			try {
				caCert = CertificateUtils.loadCertificate(new FileInputStream(
						caFile), Encoding.PEM);
				
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				continue;
			}

			String caDN = DNUtil.getOpenSSLSubject(caCert
					.getSubjectX500Principal());

			log.debug("Checking CA: " + caDN);

			dao.createIfMissing(caDN, null);

		}
	}
}
