/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.core.tasks;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.event.EventDispatcher;
import org.glite.security.voms.admin.event.request.VOMembershipRequestExpiredEvent;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.model.request.NewVOMembershipRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpiredRequestsPurgerTask implements Runnable,
  RegistrationServiceTask {

  private static final Logger log = LoggerFactory
    .getLogger(ExpiredRequestsPurgerTask.class);

  private long requestLifetime;

  private boolean warnUsers;
  
  private final DAOFactory daoFactory;
  private final EventDispatcher eventDispatcher;
  

  public ExpiredRequestsPurgerTask(DAOFactory daoFactory, EventDispatcher dispatcher) {

    this.daoFactory = daoFactory;
    this.eventDispatcher = dispatcher;
    
    // FIXME: Configuration should be injected
    VOMSConfiguration conf = VOMSConfiguration.instance();

    requestLifetime = conf.getLong(
      VOMSConfigurationConstants.UNCONFIRMED_REQUESTS_EXPIRATION_TIME,
      TimeUnit.DAYS.toSeconds(7));

    warnUsers = conf.getBoolean(
      VOMSConfigurationConstants.VO_MEMBERSHIP_UNCONFIRMED_REQ_WARN_POLICY,
      false);

  }

  public void run() {

    if (requestLifetime < 0) {
      log
        .debug("Request purger NOT STARTED since a negative lifetime for requests was set in configuration.");
      return;
    }

    RequestDAO dao = daoFactory.getRequestDAO();

    List<NewVOMembershipRequest> expiredRequests = dao
      .findExpiredVOMembershipRequests();

    for (NewVOMembershipRequest req : expiredRequests) {

      log
        .info(
          "Removing unconfirmed request '{}' from database since the confirmation period has expired.",
          req);
      
      dao.makeTransient(req);

      if (warnUsers){
        eventDispatcher.dispatch(new VOMembershipRequestExpiredEvent(req));
      }

    }

  }

}
