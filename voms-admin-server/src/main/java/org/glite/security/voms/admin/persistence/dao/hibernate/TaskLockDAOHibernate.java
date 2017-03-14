package org.glite.security.voms.admin.persistence.dao.hibernate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.configuration.ServiceID;
import org.glite.security.voms.admin.persistence.dao.generic.TaskLockDAO;
import org.glite.security.voms.admin.persistence.model.task.TaskLock;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskLockDAOHibernate extends GenericHibernateDAO<TaskLock, String>
  implements TaskLockDAO {

  public static final Logger LOG = LoggerFactory
    .getLogger(TaskLockDAOHibernate.class);

  private static final String GET_LOCK_QUERY = "from TaskLock t where t.taskName = :taskName";
  private static final int QUERY_TIMEOUT_IN_SECS = 1;

  private void logAcquireTaskLock(String taskName, TaskLock lock) {

    LOG.debug("Acquiring lock for task {} at {}. TaskLock: {}",
      new Object[] { taskName, new Date(), lock });

  }

  private void reassignLockToThisService(TaskLock lock) {

    lock.setServiceId(ServiceID.getServiceID());
    lock.setCreatedAt(new Date());
    lock.setFinishedAt(null);
    makePersistent(lock);
  }

  private TaskLock createLock(String taskName) {

    TaskLock lock = new TaskLock();
    lock.setTaskName(taskName);
    lock.setServiceId(ServiceID.getServiceID());
    lock.setCreatedAt(new Date());
    makePersistent(lock);

    return lock;
  }

  private TaskLock findLock(String taskName) {

    return (TaskLock) getSession().createQuery(GET_LOCK_QUERY)
      .setLockMode("t", LockMode.UPGRADE)
      .setTimeout(QUERY_TIMEOUT_IN_SECS)
      .setString("taskName", taskName)
      .uniqueResult();
  }

  private boolean lockHasExpired(TaskLock lock, long taskPeriodInSecs) {

    if (lock.isTaskDone()) {
      return false;
    }

    final long now = System.currentTimeMillis();
    final long lockExpirationTime = lock.getCreatedAt()
      .getTime() + 2 * TimeUnit.SECONDS.toMillis(taskPeriodInSecs);

    return (now > lockExpirationTime);
  }

  private boolean taskAlreadyRanInPeriod(TaskLock lock, long taskPeriodInSecs) {

    if (!lock.isTaskDone()) {
      return false;
    }

    final long now = System.currentTimeMillis();
    
    // Actually the next scheduled execution time should be calculated
    // taking into account the task creation time, but we take the last 
    // completion time here to have some margin. 
    final long nextScheduledExeutionTime = lock.getFinishedAt()
      .getTime() + TimeUnit.SECONDS.toMillis(taskPeriodInSecs);

    return (now < nextScheduledExeutionTime && 
      !lock.getServiceId().equals(ServiceID.getServiceID()));
  }

  @Override
  public TaskLock acquireLock(String taskName, long taskPeriod) {

    Validate.notEmpty(taskName);

    TaskLock lock = findLock(taskName);

    if (lock == null) {
      lock = createLock(taskName);
      logAcquireTaskLock(taskName, lock);
      return lock;
    }

    // A lock exists for this task
    LOG.debug("Lock found for task {}: {}", taskName, lock);

    if (lock.isTaskDone()) {
      if (taskPeriod > 0 && taskAlreadyRanInPeriod(lock, taskPeriod)) {
        LOG.info(
          "Backing off. Task {} was last finished at {} on service {} and period is {} "
            + "seconds",
          new Object[] { lock.getTaskName(), lock.getFinishedAt(),
            lock.getServiceId(), taskPeriod });
        return null;
      }

      reassignLockToThisService(lock);
      logAcquireTaskLock(taskName, lock);
      return lock;
    }

    // Task is not done yet. Check if the lock has expired (only if taskPeriod
    // was set)
    if (taskPeriod > 0 && lockHasExpired(lock, taskPeriod)) {

      LOG.warn(
        "Task {} has a lock created at time {} that's not finished at {}. "
          + "Acquiring it",
        new Object[] { lock.getTaskName(), lock.getCreatedAt(), new Date() });

      reassignLockToThisService(lock);
      logAcquireTaskLock(taskName, lock);
      return lock;
    }

    // Task not done and lock not expired, backoff
    LOG.debug("Backing off since lock {} is still active", lock);
    return null;
  }

  @Override
  public TaskLock releaseLock(TaskLock lock) {

    Validate.notNull(lock);

    final String taskName = lock.getTaskName();

    // Refresh lock from the database
    lock = findLock(taskName);

    if (lock == null) {
      LOG.error(
        "Could not release lock for task {}, as no lock was found for such task",
        taskName);
      return null;
    }

    if (lock.isTaskDone()) {
      LOG.error(
        "Could not release lock for task {}, as lock has already been released on {}",
        taskName, lock.getFinishedAt());
      return null;
    }

    if (!lock.getServiceId()
      .equals(ServiceID.getServiceID())) {
      LOG.error(
        "Could not release lock for task {}, as lock is currently owned by service {}",
        taskName, lock.getServiceId());
      return null;
    }

    Date now = new Date();
    lock.setFinishedAt(now);
    makePersistent(lock);

    LOG.debug("Lock {} released succesfully", lock);
    return lock;
  }

  @Override
  public TaskLock acquireLock(String taskName) {

    return acquireLock(taskName, -1L);
  }

}
