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
package org.glite.security.voms.admin.core.validation.strategies.impl;

import java.util.List;

import org.glite.security.voms.admin.core.validation.strategies.HandleAUPFailingMembersStrategy;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.aup.SignAUPTaskAssignedEvent;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskDAO;
import org.glite.security.voms.admin.persistence.dao.hibernate.HibernateDAOFactory;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoOpAUPFailingMembersStrategy implements
  HandleAUPFailingMembersStrategy {

  public static final Logger LOG = LoggerFactory
    .getLogger(NoOpAUPFailingMembersStrategy.class);

  @Override
  public void handleAUPFailingMembers(List<VOMSUser> aupFailingMembers) {
    for (VOMSUser u: aupFailingMembers){
      try{
        
        handleAUPFailingMember(u);
        
      }catch(Throwable t){
        LOG.error("Error handling AUP failing member {}", u, t);
      }
    }
  }

  public void handleAUPFailingMember(VOMSUser u) {

    AUPDAO aupDAO = HibernateDAOFactory.instance().getAUPDAO();
    AUP aup = aupDAO.getVOAUP();

    LOG.debug("Checking user '{}' compliance with '{}'", u, aup.getName());

    TaskDAO taskDAO = DAOFactory.instance().getTaskDAO();

    SignAUPTask pendingSignAUPTask = u.getPendingSignAUPTask(aup);

    if (pendingSignAUPTask == null) {

      SignAUPTask t = taskDAO.createSignAUPTask(aup);
      u.assignTask(t);
      LOG.info("Sign aup task assigned to user '{}'. Will expire on {}", u,
        t.getExpiryDate());

      EventManager.instance().dispatch(new SignAUPTaskAssignedEvent(u, aup, t));

    }
  }

}
