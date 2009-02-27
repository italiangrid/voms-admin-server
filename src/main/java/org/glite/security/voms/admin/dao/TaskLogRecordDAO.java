package org.glite.security.voms.admin.dao;

import java.util.List;

import org.glite.security.voms.admin.model.task.LogRecord;
import org.glite.security.voms.admin.model.task.Task;


public interface TaskLogRecordDAO extends GenericDAO <LogRecord, Long> {
    
    public List <LogRecord> findForTask(Task t);
    public void deleteForTask(Task t);
    

}
