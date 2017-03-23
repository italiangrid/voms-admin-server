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
package org.glite.security.voms.admin.core.tasks;

import java.util.List;

import org.glite.security.voms.admin.event.EventDispatcher;
import org.glite.security.voms.admin.event.user.aup.SignAUPTaskCanceledEvent;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskDAO;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CancelSignAUPTasksForExpiredUsersTask
  implements Runnable, RegistrationServiceTask {

  public static final Logger LOGGER = LoggerFactory
    .getLogger(CancelSignAUPTasksForExpiredUsersTask.class);

  public static final String CANCELLATION_REASON = "User is suspended and his/her membership has expired";

  private final DAOFactory daoFactory;
  private final EventDispatcher eventDispatcher;

  public CancelSignAUPTasksForExpiredUsersTask(DAOFactory daoFactory,
    EventDispatcher dispatcher) {
    this.daoFactory = daoFactory;
    this.eventDispatcher = dispatcher;
  }

  @Override
  public void run() {

    TaskDAO dao = daoFactory.getTaskDAO();

    List<SignAUPTask> activeAUPTask = dao.findActiveSignAUPTasks();

    for (SignAUPTask t : activeAUPTask) {

      VOMSUser user = t.getUser();

      if (user.hasExpired() && user.isSuspended()) {
        LOGGER.info(
          "Canceling Sign AUP task {} for user {} as his/her membership has "
          + "expired and the user is currently suspended",
          t, user);
        t.setCompleted();
        eventDispatcher
          .dispatch(new SignAUPTaskCanceledEvent(t, CANCELLATION_REASON));
      }

    }
  }

}
