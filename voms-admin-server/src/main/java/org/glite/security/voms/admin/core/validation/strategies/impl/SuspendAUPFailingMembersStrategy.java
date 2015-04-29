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

package org.glite.security.voms.admin.core.validation.strategies.impl;

import java.util.List;

import org.glite.security.voms.admin.core.validation.ValidationManager;
import org.glite.security.voms.admin.core.validation.strategies.AUPFailingMembersLookupStrategy;
import org.glite.security.voms.admin.core.validation.strategies.HandleAUPFailingMembersStrategy;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.aup.SignAUPTaskAssignedEvent;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskDAO;
import org.glite.security.voms.admin.persistence.dao.hibernate.HibernateDAOFactory;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUser.SuspensionReason;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.glite.security.voms.admin.persistence.model.task.Task;
import org.glite.security.voms.admin.persistence.model.task.Task.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuspendAUPFailingMembersStrategy implements
  HandleAUPFailingMembersStrategy, AUPFailingMembersLookupStrategy {

  public static final Logger log = LoggerFactory
    .getLogger(SuspendAUPFailingMembersStrategy.class);

  public List<VOMSUser> findAUPFailingMembers() {

    AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();
    VOMSUserDAO userDAO = VOMSUserDAO.instance();

    return userDAO.findAUPFailingUsers(aupDAO.getVOAUP());

  }

  protected synchronized void handleAUPFailingMember(VOMSUser u) {

    AUPDAO aupDAO = HibernateDAOFactory.instance().getAUPDAO();
    AUP aup = aupDAO.getVOAUP();

    log.debug("Checking user '" + u + "' compliance with '" + aup.getName()
      + "'");
    TaskDAO taskDAO = DAOFactory.instance().getTaskDAO();

    SignAUPTask pendingSignAUPTask = u.getPendingSignAUPTask(aup);

    if (pendingSignAUPTask == null) {

      SignAUPTask t = taskDAO.createSignAUPTask(aup);
      u.assignTask(t);
      log.info("Sign aup task assigned to user '{}'. Will expire on {}", u,
        t.getExpiryDate());
      
      EventManager.instance().dispatch(new SignAUPTaskAssignedEvent(u, aup,t));

    } else {

      if (u.isSuspended()) {

        log
          .debug("User already suspended. Reason: {}", u.getSuspensionReason());
        return;
      }

      // User is not suspended, look if expired the pending Sign AUP task
      // has expired

      log.debug("Sign AUP task: {}", pendingSignAUPTask);

      if (pendingSignAUPTask.getStatus().equals(TaskStatus.EXPIRED)) {

        log.info("Suspending user '" + u + "' that failed to sign AUP in time");

        ValidationManager.instance().suspendUser(u,
          SuspensionReason.FAILED_TO_SIGN_AUP);
      }
    }

  }

  public void handleAUPFailingMembers(List<VOMSUser> aupFailingMembers) {

    if (aupFailingMembers == null || aupFailingMembers.isEmpty()) {
      return;
    }

    for (VOMSUser u : aupFailingMembers)
      handleAUPFailingMember(u);

  }
}
