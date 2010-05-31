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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.util.FileCertReader;
import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.error.VOMSException;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.VOMSCADAO;
import org.glite.security.voms.admin.persistence.error.VOMSDatabaseException;
import org.glite.security.voms.admin.persistence.model.VOMSCA;
import org.glite.security.voms.admin.util.DNUtil;

/**
 * @author <a href="mailto:andrea.ceccanti@cnaf.infn.it">Andrea Ceccanti</a>
 * @author <a href="mailto:Akos.Frohner@cern.ch">Akos Frohner</a>
 * @author <a href="mailto:lorentey@elte.hu">Karoly Lorentey</a>
 * 
 * 
 */
public final class UpdateCATask extends TimerTask {

	static final Logger log = LoggerFactory.getLogger(UpdateCATask.class);

	static private UpdateCATask instance = null;

	protected Timer timer;

	public static UpdateCATask instance(Timer theTimer) {

		if (instance == null)
			instance = new UpdateCATask(theTimer);

		return instance;
	}

	private UpdateCATask(Timer theTimer) {

		timer = theTimer;
		// Configure UpdateCATask
		VOMSConfiguration config = VOMSConfiguration.instance();

		if (config.getBoolean(VOMSConfigurationConstants.READONLY, false)) {
			log
					.info("CA update thread not started.  This is a read-only VOMS Admin instance");
			return;
		}

		if (timer != null) {
			long period = config.getLong(VOMSConfigurationConstants.CAFILES_PERIOD, -1);

			if (period > 0) {

				log.info("Scheduling UpdateCATask with period: " + period
						+ " seconds.");

				timer.schedule(this, 60 * 1000, period * 1000);

			} else {

				log
						.info("CA update thread not started. voms.cafiles.period is negative or undefined.");
				return;
			}
		}

	}

	public void run() {

		log.info("CA update thread started...");
		HibernateFactory.beginTransaction();
		updateCAs();
		HibernateFactory.commitTransaction();
		HibernateFactory.closeSession();
		log.info("CA update thread done.");

	}

	public synchronized static void updateCAs() {

		String caFiles = VOMSConfiguration.instance().getString(
				VOMSConfigurationConstants.CAFILES,
				"/etc/grid-security/certificates/*.0");

		log.info("Updating CAs from: " + caFiles);

		if (caFiles != null) {

			try {

				VOMSCADAO dao = VOMSCADAO.instance();
				FileCertReader certReader = new FileCertReader();

				Vector cas = certReader.readAnchors(caFiles);

				Iterator caIter = cas.iterator();

				List<VOMSCA> knownCAs = dao.getValid();

				while (caIter.hasNext()) {

					TrustAnchor anchor = (TrustAnchor) caIter.next();
					X509Certificate caCert = anchor.getTrustedCert();

					String caDN = DNUtil.getBCasX500(caCert
							.getSubjectX500Principal());

					log.debug("Checking CA: " + caDN);

					boolean foundCA = false;

					for (VOMSCA knownCA : knownCAs)
						if (knownCA.getDn().equals(caDN)) {
							foundCA = true;
							log
									.debug(caDN
											+ " is already in the trusted CA database.");
						}

					if (!foundCA)
						dao.createCA(caDN, null);

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
				throw new VOMSException("Error updating trusted CA database!",
						e);

			}

		}

	}
}
