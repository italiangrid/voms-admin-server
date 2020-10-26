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

  public static final int DEFAULT_BATCH_SIZE = 50;

  final CleanupUserLookupStrategy lookupStrategy;
  int batchSize = DEFAULT_BATCH_SIZE;
  VOMSUserDAO dao;
  EventManager manager;

  public ExpiredUserCleanupTask(CleanupUserLookupStrategy lookupStrategy, int batchSize) {
    this.lookupStrategy = lookupStrategy;
    this.batchSize = batchSize;
  }

  @Override
  public void run() {

    if (dao == null) {
      dao = VOMSUserDAO.instance();
    }

    if (manager == null) {
      manager = EventManager.instance();
    }

    int processedUsers = 0;
    ScrollableResults userCursor = lookupStrategy.findUsersEligibleForCleanup();

    while (userCursor.next() && processedUsers < batchSize) {
      VOMSUser user = (VOMSUser) userCursor.get(0);

      if (user.getTasks().isEmpty()) {
        LOG.info("Deleting user '{} ({})' who expired on '{}'", user.getShortName(), user.getId(),
            user.getEndTime());

        dao.delete(user);
        manager.dispatch(new UserCleanedUpEvent(user));
      } else {
        LOG.info("Cleaning up tasks for user '{} ({})' who expired on '{}'", user.getShortName(), user.getId(),
            user.getEndTime());
        dao.deleteTasks(user);
      }

      processedUsers++;
    }
  }


  public void setDao(VOMSUserDAO dao) {
    this.dao = dao;
  }

  public void setManager(EventManager manager) {
    this.manager = manager;
  }
}
