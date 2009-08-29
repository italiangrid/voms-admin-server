package org.glite.security.voms.admin.dao.generic;

import org.glite.security.voms.admin.model.task.TaskType;

public interface TaskTypeDAO extends GenericDAO<TaskType, Long> {

	public TaskType findByName(String name);
}
