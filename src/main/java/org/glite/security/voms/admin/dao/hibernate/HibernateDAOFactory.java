package org.glite.security.voms.admin.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.dao.generic.GroupDAO;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.AUPVersionDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.RequesterInfoDAO;
import org.glite.security.voms.admin.dao.generic.TagDAO;
import org.glite.security.voms.admin.dao.generic.TaskDAO;
import org.glite.security.voms.admin.dao.generic.TaskLogRecordDAO;
import org.glite.security.voms.admin.dao.generic.TaskTypeDAO;
import org.glite.security.voms.admin.dao.generic.UserDAO;
import org.glite.security.voms.admin.model.AUPVersion;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.RequesterInfo;
import org.glite.security.voms.admin.model.task.LogRecord;
import org.glite.security.voms.admin.model.task.Task;
import org.glite.security.voms.admin.model.task.TaskType;
import org.hibernate.criterion.Restrictions;

/**
 * Returns Hibernate-specific instances of DAOs.
 * <p/>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * a nested static class to implement the interface in a generic way. This
 * allows clean refactoring later on, should the interface implement business
 * data access methods at some later time. Then, we would externalize the
 * implementation into its own first-level class.
 * 
 * @author Christian Bauer
 * @author Andrea Ceccanti
 */
public class HibernateDAOFactory extends DAOFactory {

	// Inline concrete DAO implementations with no business-related data access
	// methods.
	// If we use public static nested classes, we can centralize all of them in
	// one source file.
	public static class AUPVersionDAOHibernate extends
			GenericHibernateDAO<AUPVersion, Long> implements AUPVersionDAO {
	}

	public static class GroupDAOHibernate extends
			NamedEntityHibernateDAO<VOMSGroup, Long> implements GroupDAO {
	}

	public static class UserDAOHibernate extends
			NamedEntityHibernateDAO<VOMSUser, Long> implements UserDAO {
	}

	public static class RequesterInfoDAOHibernate extends
			GenericHibernateDAO<RequesterInfo, Long> implements
			RequesterInfoDAO {

	}

	public static class TaskLogRecordDAOHibernate extends
			GenericHibernateDAO<LogRecord, Long> implements TaskLogRecordDAO {

		public void deleteForTask(Task t) {

		}

		public List<LogRecord> findForTask(Task t) {

			LogRecord lr = new LogRecord();
			lr.setTask(t);

			return findByCriteria(Restrictions.eq("task", t));
		}

	}

	public static class TaskTypeDAOHibernate extends
			NamedEntityHibernateDAO<TaskType, Long> implements TaskTypeDAO {

	}

	private static Log log = LogFactory.getLog(HibernateDAOFactory.class);

	@Override
	public AUPDAO getAUPDAO() {
		return (AUPDAO) instantiateDAO(AUPDAOHibernate.class);
	}

	@Override
	public AUPVersionDAO getAUPVersionDAO() {
		return (AUPVersionDAO) instantiateDAO(AUPVersionDAOHibernate.class);
	}

	@Override
	public GroupDAO getGroupDAO() {
		return (GroupDAO) instantiateDAO(GroupDAOHibernate.class);
	}

	@Override
	public RequestDAO getRequestDAO() {

		return (RequestDAO) instantiateDAO(RequestDAOHibernate.class);
	}

	@Override
	public RequesterInfoDAO getRequesterInfoDAO() {

		return (RequesterInfoDAO) instantiateDAO(RequesterInfoDAOHibernate.class);

	}

	@Override
	public TagDAO getTagDAO() {

		return (TagDAO) instantiateDAO(TagDAOHibernate.class);

	}

	@Override
	public TaskDAO getTaskDAO() {

		return (TaskDAO) instantiateDAO(TaskDAOHibernate.class);

	}

	public TaskLogRecordDAO getTaskLogRecordDAO() {

		return (TaskLogRecordDAO) instantiateDAO(TaskLogRecordDAOHibernate.class);
	}

	@Override
	public TaskTypeDAO getTaskTypeDAO() {

		return (TaskTypeDAO) instantiateDAO(TaskTypeDAOHibernate.class);
	}

	public UserDAO getUserDAO() {
		return (UserDAO) instantiateDAO(UserDAOHibernate.class);
	}

	private GenericHibernateDAO instantiateDAO(Class daoClass) {
		try {
			log.debug("Instantiating DAO: " + daoClass);
			return (GenericHibernateDAO) daoClass.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException("Can not instantiate DAO: " + daoClass,
					ex);
		}
	}

}
