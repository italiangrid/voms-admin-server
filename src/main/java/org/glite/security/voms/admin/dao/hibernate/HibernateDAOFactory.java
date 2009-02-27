package org.glite.security.voms.admin.dao.hibernate;

import java.util.List;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.dao.AUPDAO;
import org.glite.security.voms.admin.dao.AUPVersionDAO;
import org.glite.security.voms.admin.dao.DAOFactory;
import org.glite.security.voms.admin.dao.TagDAO;
import org.glite.security.voms.admin.dao.TaskDAO;
import org.glite.security.voms.admin.dao.TaskLogRecordDAO;
import org.glite.security.voms.admin.dao.TaskTypeDAO;
import org.glite.security.voms.admin.model.AUPVersion;
import org.glite.security.voms.admin.model.Tag;
import org.glite.security.voms.admin.model.task.LogRecord;
import org.glite.security.voms.admin.model.task.Task;
import org.glite.security.voms.admin.model.task.TaskType;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

/**
 * Returns Hibernate-specific instances of DAOs.
 * <p/>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * a nested static class to implement the interface in a generic way. This allows clean
 * refactoring later on, should the interface implement business data access
 * methods at some later time. Then, we would externalize the implementation into
 * its own first-level class.
 *
 * @author Christian Bauer
 * @author Andrea Ceccanti
 */
public class HibernateDAOFactory extends DAOFactory {

    private static Log log = LogFactory.getLog(HibernateDAOFactory.class);

    private GenericHibernateDAO instantiateDAO(Class daoClass) {
        try {
            log.debug("Instantiating DAO: " + daoClass);
            return (GenericHibernateDAO)daoClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Can not instantiate DAO: " + daoClass, ex);
        }
    }

    @Override
    public AUPDAO getAUPDAO() {
        return (AUPDAO)instantiateDAO( AUPDAOHibernate.class);
    }

    
    @Override
    public AUPVersionDAO getAUPVersionDAO() {
        return (AUPVersionDAO)instantiateDAO( AUPVersionDAOHibernate.class );
    }
    
    @Override
    public TagDAO getTagDAO() {

        return (TagDAO)instantiateDAO( TagDAOHibernate.class );
        
    }
    
    @Override
    public TaskDAO getTaskDAO() {

        return (TaskDAO)instantiateDAO( TaskDAOHibernate.class );
        
    }

    @Override
    public TaskTypeDAO getTaskTypeDAO() {

        return (TaskTypeDAO) instantiateDAO( TaskTypeDAOHibernate.class );
    }
    
    
    public TaskLogRecordDAO getTaskLogRecordDAO(){
        
        return (TaskLogRecordDAO)instantiateDAO( TaskLogRecordDAOHibernate.class );
    }
    
    // Inline concrete DAO implementations with no business-related data access methods.
    // If we use public static nested classes, we can centralize all of them in one source file.
    public static class AUPVersionDAOHibernate 
        extends GenericHibernateDAO <AUPVersion, Long>
        implements AUPVersionDAO{}

    
    public static class TaskTypeDAOHibernate
        extends NamedEntityHibernateDAO <TaskType, Long>
        implements TaskTypeDAO{
        
    }
    
    public static class TaskLogRecordDAOHibernate
        extends GenericHibernateDAO <LogRecord, Long>
        implements TaskLogRecordDAO{

        public void deleteForTask( Task t ) {

            
            
        }

        public List <LogRecord> findForTask( Task t ) {
            
            LogRecord lr = new LogRecord();
            lr.setTask( t );
            
            return findByCriteria( Restrictions.eq( "task", t ) );
        }
        
//        @Override
//        public void makeTransient( LogRecord entity ) {
//        
//            entity.setTask( null );
//            super.makeTransient( entity );
//        }
        
        
    }




    
    
    

}
