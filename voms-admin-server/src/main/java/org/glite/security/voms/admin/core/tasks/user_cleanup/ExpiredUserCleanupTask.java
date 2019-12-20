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
package org.glite.security.voms.admin.core.tasks.user_cleanup;

import org.glite.security.voms.admin.core.tasks.RegistrationServiceTask;
import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.UserCleanedUpEvent;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpiredUserCleanupTask implements Runnable, RegistrationServiceTask {
  public static final Logger LOG = LoggerFactory.getLogger(ExpiredUserCleanupTask.class);

  final CleanupUserLookupStrategy lookupStrategy;
  VOMSUserDAO dao;
  EventManager eventManager;

  public ExpiredUserCleanupTask(CleanupUserLookupStrategy lookupStrategy) {
    this.lookupStrategy = lookupStrategy;
  }

  @Override
  public void run() {

    if (dao == null) {
      dao = VOMSUserDAO.instance();
    }

    if (eventManager == null) {
      eventManager = EventManager.instance();
    }

    ScrollableResults userCursor = lookupStrategy.findUsersEligibleForCleanup();

    while (userCursor.next()) {
      VOMSUser user = (VOMSUser) userCursor.get(0);

      LOG.debug("Cleaning up user '{}' who expired on '{}'", user.getShortName(),
          user.getEndTime());
      dao.delete(user);
      eventManager.dispatch(new UserCleanedUpEvent(user));
    }
  }

  public void setDao(VOMSUserDAO dao) {
    this.dao = dao;
  }

  public void setEventManager(EventManager eventManager) {
    this.eventManager = eventManager;
  }
}
