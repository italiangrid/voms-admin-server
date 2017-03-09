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

import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskLockDAO;
import org.glite.security.voms.admin.persistence.error.VOMSDatabaseException;
import org.glite.security.voms.admin.persistence.model.task.TaskLock;
import org.glite.security.voms.admin.servlets.InitSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseTransactionTaskWrapper extends BaseTaskWrapper {

  public static final Logger log = LoggerFactory
    .getLogger(DatabaseTransactionTaskWrapper.class);

  final boolean doLogging;
  final boolean doAcquireLock;
  final long periodInSecs;

  
  public DatabaseTransactionTaskWrapper(Runnable task, boolean logStartAndEnd,
    boolean acquireLock) {

    this(task, logStartAndEnd, acquireLock, -1);
  }
  
  public DatabaseTransactionTaskWrapper(Runnable task, boolean logStartAndEnd,
    boolean acquireLock, long periodInSecs) {

    super(task);
    doLogging = logStartAndEnd;
    this.doAcquireLock = acquireLock;
    this.periodInSecs = periodInSecs;

  }

  private TaskLock acquireLock() {

    TaskLock lock = null;

    try {

      HibernateFactory.beginTransaction();
      InitSecurityContext.setInternalAdminContext();

      final TaskLockDAO dao = DAOFactory.instance()
        .getTaskLockDAO();

      lock = dao.acquireLock(task.getClass()
        .getSimpleName(), periodInSecs);

      if (lock == null) {
        log.warn("Could not acquire lock for task {}", task.getClass()
          .getSimpleName());
        return null;
      }

      log.debug("Lock acquired for task {}", lock.getTaskName());

      return lock;

    } catch (Throwable ex) {
      log.error("Error trying to acquire lock for task {}", task.getClass()
        .getSimpleName(), ex);
    } finally {
      HibernateFactory.commitTransaction();
    }

    log.warn("Could not acquire lock for task {}", task.getClass()
      .getSimpleName());

    return null;
  }

  private void releaseLock(TaskLock lock) {
    
    if (lock == null){
      return;
    }
    
    try {
      
      HibernateFactory.beginTransaction();

      final TaskLockDAO dao = DAOFactory.instance()
        .getTaskLockDAO();

      dao.releaseLock(lock);
      HibernateFactory.commitTransaction();
      log.debug("Lock for task {} released succesfully", lock.getTaskName());

    } catch (Throwable ex) {
      log.error("Error releasing lock {}", lock, ex);
    }
  }

  public void run() {

    TaskLock lock = null;
    final long startTime = System.currentTimeMillis();

    try {

      InitSecurityContext.setInternalAdminContext();
      if (doLogging) {
        log.info("{} task starting...", task.getClass()
          .getSimpleName());
      }

      if (doAcquireLock) {
        lock = acquireLock();
        if (lock == null) {
          return;
        }
      }

      HibernateFactory.beginTransaction();

      task.run();

      HibernateFactory.commitTransaction();

    } catch (VOMSDatabaseException e) {

      log.error("Database exception caught while executing {} task: {}",
        new String[] { task.getClass()
          .getSimpleName(), e.getMessage() });
      log.error(e.getMessage(), e);
      log.error("Swallowing the exception hoping it's a temporary failure.");

    } catch (Throwable t) {

      log.error("An unexpected exception was caught while executing task {}",
        task.getClass()
          .getSimpleName());
      log.error(t.getMessage(), t);

    } finally {

      if (doAcquireLock) {  
        releaseLock(lock);
      }

      HibernateFactory.closeSession();
      final long endTime = System.currentTimeMillis();
      final long taskRunningTime = endTime - startTime;

      if (doLogging) {
        log.info("{} task done in {} msec", task.getClass()
          .getSimpleName(), taskRunningTime);
      }
    }
  }
 
}
