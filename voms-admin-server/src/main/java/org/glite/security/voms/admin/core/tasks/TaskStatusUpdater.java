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

import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskDAO;
import org.glite.security.voms.admin.persistence.model.task.Task;
import org.glite.security.voms.admin.persistence.model.task.Task.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskStatusUpdater implements Runnable, RegistrationServiceTask {

  public static Logger log = LoggerFactory.getLogger(TaskStatusUpdater.class);

  public void run() {

    TaskDAO taskDAO = DAOFactory.instance().getTaskDAO();

    List<Task> activeTasks = taskDAO.getActiveTasks();

    for (Task t : activeTasks) {

      Date now = new Date();

      if (t.getExpiryDate().before(now)) {
        log.debug("Task " + t + " has expired, setting its status to EXPIRED.");
        t.setStatus(TaskStatus.EXPIRED);
      }
    }
  }

}
