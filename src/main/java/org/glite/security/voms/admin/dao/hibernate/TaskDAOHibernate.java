package org.glite.security.voms.admin.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.dao.DAOFactory;
import org.glite.security.voms.admin.dao.TaskDAO;
import org.glite.security.voms.admin.dao.TaskLogRecordDAO;
import org.glite.security.voms.admin.dao.TaskTypeDAO;
import org.glite.security.voms.admin.database.VOMSDatabaseException;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.task.ApproveUserRequestTask;
import org.glite.security.voms.admin.model.task.LogRecord;
import org.glite.security.voms.admin.model.task.SignAUPTask;
import org.glite.security.voms.admin.model.task.Task;
import org.glite.security.voms.admin.model.task.TaskType;
import org.glite.security.voms.admin.model.task.Task.TaskStatus;


public class TaskDAOHibernate extends GenericHibernateDAO <Task, Long> implements TaskDAO{

    
    private static final Log log = LogFactory.getLog( TaskDAOHibernate.class );
    
    protected void addTaskLogRecord(Task t, TaskStatus event, Date date, VOMSUser u, VOMSAdmin a){
        
        TaskLogRecordDAO tlrDAO = DAOFactory.instance( DAOFactory.HIBERNATE ).getTaskLogRecordDAO();
        
        LogRecord lr = new LogRecord();
        
        lr.setTask( t );
        lr.setEvent( event );
        lr.setUser( u );
        lr.setAdmin( a );
        lr.setDate( date );
        
        tlrDAO.makePersistent( lr );
        
    }

    public ApproveUserRequestTask createApproveUserRequestTask( Request req ) {

        // Look for already existing tasks for the same request? 
        return null;
    }
    
    public SignAUPTask createSignAUPTask( AUP aup, Date expiryDate ) {

        TaskTypeDAO ttDAO = DAOFactory.instance( DAOFactory.HIBERNATE ).getTaskTypeDAO();
        
        if ( aup == null )
            throw new NullArgumentException( "aup cannot be null!" );
        
        if ( expiryDate == null )
            throw new NullArgumentException( "expiryDate cannot be null!" );
        
        
        TaskType tt = ttDAO.findByName( "SignAUPTask" );
        
        if (tt == null)
            throw new VOMSDatabaseException("Inconsistent database! Task type for SignAUPTask not found in database!");
        
        SignAUPTask t = new SignAUPTask(tt,aup,expiryDate);
        
        makePersistent( t );
        
        addTaskLogRecord( t, TaskStatus.CREATED, t.getCreationDate(), null, null );
        
        return t; 
        
    }
    
    public List <Task> findApproveUserRequestTasks() {

        return findConcrete( ApproveUserRequestTask.class );
        
    }

    protected List<Task> findConcrete(Class clazz){
        return getSession().createCriteria( clazz ).list();
    }

    public List <Task> findSignAUPTasks() {

        return findConcrete( SignAUPTask.class );
    }

    
    public void removeAllTasks() {

        List<Task> tasks = findAll();
        
        for (Task t: tasks){
            
            makeTransient( t );
        }
        
        
    }

    
    public void removeTaskLogRecords(Task t){
        
        TaskLogRecordDAO tlrDAO = DAOFactory.instance( DAOFactory.HIBERNATE ).getTaskLogRecordDAO();
        
        for (LogRecord lr: tlrDAO.findForTask( t ))
            tlrDAO.makeTransient( lr );
        
        
    }
    
    
    public void setApproveUserRequestTaskCompleted( ApproveUserRequestTask t,
            VOMSAdmin a ) {

        t.setCompleted();
        addTaskLogRecord( t, t.getStatus(), t.getCompletionDate(), null, a );
        
    }

    public void setSignAUPTaskCompleted( SignAUPTask t, VOMSUser u ) {
        
        t.setCompleted();
        addTaskLogRecord( t, t.getStatus(), t.getCompletionDate(), u, null );
        
        
    }
    
    public void deleteSignAUPTask(SignAUPTask t){
        getSession().delete( t );
    }
    
    
}
