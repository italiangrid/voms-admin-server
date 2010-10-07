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

package org.glite.security.voms.admin.integration.orgdb;

import java.util.List;

import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBDAOFactory;
import org.glite.security.voms.admin.integration.orgdb.dao.OrgDBVOMSPersonDAO;
import org.glite.security.voms.admin.integration.orgdb.database.OrgDBError;
import org.glite.security.voms.admin.integration.orgdb.database.OrgDBSessionFactory;
import org.glite.security.voms.admin.integration.orgdb.model.VOMSOrgDBPerson;
import org.glite.security.voms.admin.integration.orgdb.strategies.OrgDBMembershipSynchronizationStrategy;
import org.glite.security.voms.admin.integration.orgdb.strategies.OrgDBMissingMembershipRecordStrategy;
import org.glite.security.voms.admin.integration.orgdb.strategies.OrgDbExpiredParticipationStrategy;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrgDBMembershipSynchronizationTask implements Runnable {

	public static final Logger log = LoggerFactory
			.getLogger(OrgDBMembershipSynchronizationTask.class);

	protected String experimentName;

	protected OrgDBMissingMembershipRecordStrategy missingMembershipStrategy;
	protected OrgDbExpiredParticipationStrategy expiredParticipationStrategy;
	protected OrgDBMembershipSynchronizationStrategy synchronizationStrategy;
	
	
	public OrgDBMembershipSynchronizationTask(String experiment, 
			OrgDBMissingMembershipRecordStrategy invalidMembershipStrategy,
			OrgDbExpiredParticipationStrategy expiredParticaptionStrategy,
			OrgDBMembershipSynchronizationStrategy membershipSynchronizationStrategy) {
	
		experimentName = experiment;
		this.missingMembershipStrategy = invalidMembershipStrategy;
		this.expiredParticipationStrategy = expiredParticaptionStrategy;
		this.synchronizationStrategy = membershipSynchronizationStrategy;
		
	}
	
	public void run() {

		SessionFactory sf = OrgDBSessionFactory.getSessionFactory();

		try {

			OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance()
					.getVOMSPersonDAO();

			List<VOMSUser> allUsers = VOMSUserDAO.instance().findAll();

			for (VOMSUser u : allUsers) {

				VOMSOrgDBPerson orgDbPerson = dao.findPersonByEmail(u
						.getEmailAddress());

				if (orgDbPerson != null){
					
					synchronizationStrategy.synchronizeMemberInformation(u, orgDbPerson, experimentName);
					
					if (!orgDbPerson.hasValidParticipationForExperiment(experimentName))
						expiredParticipationStrategy.handleOrgDbExpiredParticipation(u, orgDbPerson, experimentName);
					
				}else {
					
					log	.warn("No OrgDB record found for user {}.", u);
					missingMembershipStrategy.handleMissingMembershipRecord(u);
				}
			}

			sf.getCurrentSession().getTransaction().commit();
			sf.getCurrentSession().close();

		} catch (OrgDBError e) {

			log.error("OrgDB exception caught: {}", e.getMessage());

			if (log.isDebugEnabled()) {
				log.error("OrgDB exception caught: {}", e.getMessage(), e);
			}

		}

	}

}
