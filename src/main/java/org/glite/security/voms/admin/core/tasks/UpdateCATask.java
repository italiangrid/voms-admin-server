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

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Vector;

import org.glite.security.util.FileCertReader;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.persistence.dao.VOMSCADAO;
import org.glite.security.voms.admin.persistence.error.VOMSDatabaseException;
import org.glite.security.voms.admin.util.DNUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a>
 * @author <a href="mailto:Akos.Frohner@cern.ch">Akos Frohner</a>
 * @author <a href="mailto:lorentey@elte.hu">Karoly Lorentey</a>
 * 
 * 
 */
public final class UpdateCATask implements Runnable{

	static final Logger log = LoggerFactory.getLogger(UpdateCATask.class);
	
	public void run(){
		
		String caFiles = VOMSConfiguration.instance().getString(
				VOMSConfigurationConstants.CAFILES,
				"/etc/grid-security/certificates/*.0");

		log.debug("Updating CAs from: " + caFiles);

		if (caFiles != null) {

			try {

				VOMSCADAO dao = VOMSCADAO.instance();
				FileCertReader certReader = new FileCertReader();

				Vector cas = certReader.readAnchors(caFiles);

				Iterator caIter = cas.iterator();

				while (caIter.hasNext()) {
									
					TrustAnchor anchor = (TrustAnchor) caIter.next();
					X509Certificate caCert = anchor.getTrustedCert();

					String caDN = DNUtil.getBCasX500(caCert
							.getSubjectX500Principal());

					log.debug("Checking CA: " + caDN);					
					dao.createIfMissing(caDN, null);

				}

			} catch (CertificateException e) {

				log
						.error(
								"Certificate parsing error while updating trusted CA database!",
								e);
				throw new VOMSException(
						"Certificate parsing error while updating trusted CA database!",
						e);

			} catch (IOException e) {
				log
						.error(
								"File access error while updating trusted CA database!",
								e);
				throw new VOMSException(
						"File access error while updating trusted CA database!",
						e);

			} catch (VOMSDatabaseException e) {

				log.error("Error updating trusted CA database!", e);
				throw e;
			}

		}

	}
}
