/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
 */
package org.glite.security.voms.admin.integration.orgdb;

import java.util.concurrent.TimeUnit;

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
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrgDBMembershipSynchronizationTask implements Runnable {

  public static final Logger log = LoggerFactory
    .getLogger(OrgDBMembershipSynchronizationTask.class);

  public static final int UPDATE_COUNT_BATCH = 50;

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

  protected VOMSOrgDBPerson lookupOrgDBPerson(VOMSUser u, Session s) {

    OrgDBVOMSPersonDAO dao = OrgDBDAOFactory.instance().getVOMSPersonDAO();
    dao.setSession(s);
    
    VOMSOrgDBPerson orgdbPerson = null;

    if (u.getOrgDbId() != null) {
      orgdbPerson = dao.findById(u.getOrgDbId(), false);

      if (orgdbPerson == null) {
        log.warn(
          "No OrgDB person found for id '{}' linked to VOMS membership {}",
          u.getOrgDbId(), u.toString());
      }
    }

    if (orgdbPerson == null) {
      log.warn("Looking up orgdb membership by user email address. User: {}",
        u.toString());
      orgdbPerson = dao.findPersonByEmail(u.getEmailAddress());
    }

    return orgdbPerson;

  }

  public void run() {

    SessionFactory sf = OrgDBSessionFactory.getSessionFactory();

    long startTime = System.currentTimeMillis();
    
    try {

      if (sf.openSession() == null){
        throw new OrgDBError("Error opening session to OrgDB");
      }
      
      sf.getCurrentSession().beginTransaction();
        
      ScrollableResults allUserCursor = VOMSUserDAO.instance()
        .findAllWithCursor();

      int updateCount = 0;

      while (allUserCursor.next()) {

        VOMSUser u = (VOMSUser) allUserCursor.get(0);

        VOMSOrgDBPerson orgDbPerson = lookupOrgDBPerson(u, 
          sf.getCurrentSession());

        if (orgDbPerson != null) {
          updateCount++;
          synchronizationStrategy.synchronizeMemberInformation(u, orgDbPerson,
            experimentName);

          if (!orgDbPerson.hasValidParticipationForExperiment(experimentName))
            expiredParticipationStrategy.handleOrgDbExpiredParticipation(u,
              orgDbPerson, experimentName);

          // Flush some updates out and release memory

          if (updateCount % UPDATE_COUNT_BATCH == 0) {

            log.debug("Flushing session after {} updates.", updateCount);

            sf.getCurrentSession().flush();
            sf.getCurrentSession().clear();
          }

        } else {

          log.warn("No OrgDB record found for user {}.", u);
          missingMembershipStrategy.handleMissingMembershipRecord(u);
        }

      }

      sf.getCurrentSession().getTransaction().commit();

    } catch (OrgDBError e) {

      log.error("OrgDB exception caught: {}", e.getMessage());

      if (log.isDebugEnabled()) {
        log.error("OrgDB exception caught: {}", e.getMessage(), e);
      }

      try {
        
        sf.getCurrentSession().getTransaction().rollback();

      } catch (Throwable t) {
        log
          .error("Error rolling back OrgDB transaction: {}", t.getMessage(), t);
      }

    } finally {

      sf.getCurrentSession().close();

      long elapsedTime = System.currentTimeMillis() - startTime;
      log.info("OrgDB synchronization took {} seconds.",
        TimeUnit.MILLISECONDS.toSeconds(elapsedTime));

    }

  }

}
