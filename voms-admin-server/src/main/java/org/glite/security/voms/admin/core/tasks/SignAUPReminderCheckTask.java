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

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.event.EventDispatcher;
import org.glite.security.voms.admin.event.user.aup.SignAUPTaskReminderEvent;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskDAO;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.glite.security.voms.admin.persistence.model.task.Task.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignAUPReminderCheckTask implements Runnable,
  RegistrationServiceTask {

  public static final Logger LOGGER = LoggerFactory
    .getLogger(SignAUPReminderCheckTask.class);

  private final List<Integer> reminders;
  private final TimeUnit timeUnit;

  private final DAOFactory daoFactory;
  private final EventDispatcher eventDispatcher;
  private final TimeProvider timeProvider;

  public SignAUPReminderCheckTask(DAOFactory daoFactory,
    EventDispatcher eventDispatcher, TimeProvider timeProvider,
    List<Integer> reminders, TimeUnit timeUnit) {

    this.daoFactory = daoFactory;
    this.reminders = reminders;
    this.timeProvider = timeProvider;
    this.timeUnit = timeUnit;
    this.eventDispatcher = eventDispatcher;
  }

  private long getNextNotificationMessageTime(SignAUPTask task) {

    Validate.notNull(task.getLastNotificationTime(),
      "task.lastNotificationTime must not be null");
    
    for (int t : reminders) {

      long expirationTime = task.getExpiryDate().getTime();
      long nextWarningTime = expirationTime - timeUnit.toMillis(t);
      
      if (nextWarningTime <= 0) {
        continue;
      }

      long lastNotificationTime = task.getLastNotificationTime().getTime();

      if (lastNotificationTime + timeUnit.toMillis(1) >= nextWarningTime) {
        continue;
      }

      return nextWarningTime;

    }

    return 0L;
  }

  public boolean needsReminder(SignAUPTask t) {

    if (t.getStatus().equals(TaskStatus.EXPIRED)) {

      return false;
    }

    if (t.getLastNotificationTime() == null) {
      LOGGER.debug("Task {} does not need a reminder: "
        + "waiting for first notification to be sent.", t.getId());
      return false;
    }

    long now = timeProvider.currentTimeMillis();

    if (now > t.getExpiryDate().getTime()) {
      LOGGER.debug("Task {} does not need a reminder: "
        + "current time is past the task expiration date.", t.getId());
      return false;
    }

    long lastNotificationTime = t.getLastNotificationTime().getTime();

    LOGGER.debug("Last notification time: {}", t.getLastNotificationTime());
    
    long nextWarningTime = getNextNotificationMessageTime(t);

    LOGGER.debug("Next expectedWarningTime: {}", new Date(nextWarningTime));
    
    if (now >= nextWarningTime && lastNotificationTime < nextWarningTime) {
      return true;
    }

    return false;
  }

  @Override
  public void run() {

    TaskDAO dao = daoFactory.getTaskDAO();

    List<SignAUPTask> activeAUPTask = dao.findActiveSignAUPTasks();

    for (SignAUPTask t : activeAUPTask) {

      LOGGER.debug("Checking whether task {} has pending, unsent reminders at instant {}.",
        t.getId(), new Date(timeProvider.currentTimeMillis()));

      if (needsReminder(t)) {

        LOGGER
          .debug(
            "Task {} needs reminder at instant {}. [expirationTime={}, lastNotificationTime={},"
              + "reminders={}, timeUnit={}]",
            new String[] {
              Long.toString(t.getId()),
              new Date(timeProvider.currentTimeMillis()).toString(),
              t.getExpiryDate().toString(),
              t.getLastNotificationTime() == null ? "<null>" : t
                .getLastNotificationTime().toString(), reminders.toString(),
              timeUnit.toString() });

        eventDispatcher.dispatch(new SignAUPTaskReminderEvent(t.getUser(), t
          .getAup(), t));

      } else {

        LOGGER
          .debug(
            "No reminder needed for Task {} at instant {}. [expirationTime={}, lastNotificationTime={},"
              + "reminders={}, timeUnit={}]",
            new String[] {
              Long.toString(t.getId()),
              new Date(timeProvider.currentTimeMillis()).toString(),
              t.getExpiryDate().toString(),
              t.getLastNotificationTime() == null ? "<null>" : t
                .getLastNotificationTime().toString(), reminders.toString(),
              timeUnit.toString() });
      }
    }

  }
}
