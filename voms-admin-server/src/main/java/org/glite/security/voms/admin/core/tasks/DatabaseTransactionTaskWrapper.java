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

import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.error.VOMSDatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseTransactionTaskWrapper extends BaseTaskWrapper {

  public static final Logger log = LoggerFactory
    .getLogger(DatabaseTransactionTaskWrapper.class);

  boolean doLogging = false;

  public DatabaseTransactionTaskWrapper(Runnable task, boolean logStartAndEnd) {

    super(task);
    doLogging = logStartAndEnd;

  }

  public void run() {

    try {

      HibernateFactory.getSession();
      HibernateFactory.beginTransaction();

      if (doLogging)
        log.debug("{} task starting...", task.getClass().getSimpleName());

      task.run();

      HibernateFactory.commitTransaction();
      HibernateFactory.closeSession();

      if (doLogging)
        log.debug("{} task done.", task.getClass().getSimpleName());

    } catch (VOMSDatabaseException e) {

      log.error("Database exception caught while executing {} task: {}",
        new String[] { task.getClass().getSimpleName(), e.getMessage() });
      log.error(e.getMessage(), e);
      log.error("Swallowing the exception hoping it's a temporary failure.");

    } catch (Throwable t) {

      log.error("An unexpected exception was caught while executing task {}",
        task.getClass().getSimpleName());
      log.error(t.getMessage(), t);

    }
  }
}
