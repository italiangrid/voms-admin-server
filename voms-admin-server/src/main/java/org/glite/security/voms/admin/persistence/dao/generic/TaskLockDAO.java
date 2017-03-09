package org.glite.security.voms.admin.persistence.dao.generic;

import org.glite.security.voms.admin.persistence.model.task.TaskLock;

public interface TaskLockDAO extends GenericDAO<TaskLock, String>{
  
  public TaskLock acquireLock(String taskName);
  
  public TaskLock acquireLock(String taskName, long taskPeriod);
  
  public TaskLock releaseLock(TaskLock lock);

}
