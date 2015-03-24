package org.glite.security.voms.admin.core.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskDAO;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.glite.security.voms.admin.persistence.model.task.Task.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendSignAUPReminderTask implements Runnable {

  public static final Logger LOGGER = LoggerFactory
    .getLogger(SendSignAUPReminderTask.class);

  private final int[] reminders;
  private final TimeUnit timeUnit;

  public SendSignAUPReminderTask(int[] reminders, TimeUnit timeUnit) {

    this.reminders = reminders;
    this.timeUnit = timeUnit;
  }

  private long getNextNotificationMessageTime(SignAUPTask task) {

    Validate.notNull(task.getLastNotificationTime(),
      "task.lastNoficationTime must not be null");

    for (int t : reminders) {

      long expirationTime = task.getExpiryDate().getTime();
      long nextWarningTime = expirationTime - timeUnit.toMillis(t);

      if (nextWarningTime < 0) {
        continue;
      }

      long lastNotificationTime = task.getLastNotificationTime().getTime();

      if (lastNotificationTime > nextWarningTime) {
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
        + "waiting for first notification to be sent.", t);
      return false;
    }

    long now = System.currentTimeMillis();

    if (now > t.getExpiryDate().getTime()) {
      LOGGER.debug("Task {} does not need a reminder: "
        + "current is time is past the task expiration date.", t);
      return false;
    }

    long nextWarningTime = getNextNotificationMessageTime(t);
    long lastNotificationTime = t.getLastNotificationTime().getTime();

    if (now >= nextWarningTime && lastNotificationTime < nextWarningTime) {
      return true;
    }

    return false;
  }

  @Override
  public void run() {

    TaskDAO dao = DAOFactory.instance().getTaskDAO();

    List<SignAUPTask> activeAUPTask = dao.findActiveSingAUPTasks();

    for (SignAUPTask t : activeAUPTask) {
      LOGGER.debug("Checking whether task {} has pending, unsent reminders.");
      
      if (needsReminder(t)) {

        LOGGER.debug(
          "Task {} needs reminder at instant {}. reminders={},timeUnit={}",
          new String[] { t.toString(),
            Long.toString(System.currentTimeMillis()),
            Arrays.toString(reminders), timeUnit.toString() });
        
        
      }
    }

  }
}
