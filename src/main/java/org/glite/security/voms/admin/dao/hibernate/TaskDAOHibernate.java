package org.glite.security.voms.admin.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.TaskDAO;
import org.glite.security.voms.admin.dao.generic.TaskTypeDAO;
import org.glite.security.voms.admin.database.VOMSDatabaseException;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.task.ApproveUserRequestTask;
import org.glite.security.voms.admin.model.task.SignAUPTask;
import org.glite.security.voms.admin.model.task.Task;
import org.glite.security.voms.admin.model.task.TaskType;
import org.glite.security.voms.admin.model.task.Task.TaskStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class TaskDAOHibernate extends GenericHibernateDAO<Task, Long> implements
		TaskDAO {

	private static final Log log = LogFactory.getLog(TaskDAOHibernate.class);

	public ApproveUserRequestTask createApproveUserRequestTask(Request req) {

		return null;
	}

	public SignAUPTask createSignAUPTask(AUP aup, Date expiryDate) {

		TaskTypeDAO ttDAO = DAOFactory.instance(DAOFactory.HIBERNATE)
				.getTaskTypeDAO();

		if (aup == null)
			throw new NullArgumentException("aup cannot be null!");

		if (expiryDate == null)
			throw new NullArgumentException("expiryDate cannot be null!");

		TaskType tt = ttDAO.findByName("SignAUPTask");

		if (tt == null)
			throw new VOMSDatabaseException(
					"Inconsistent database! Task type for SignAUPTask not found in database!");

		SignAUPTask t = new SignAUPTask(tt, aup, expiryDate);

		makePersistent(t);

		return t;

	}

	public List<Task> findApproveUserRequestTasks() {

		return findConcrete(ApproveUserRequestTask.class);

	}

	protected List<Task> findConcrete(Class clazz) {
		return getSession().createCriteria(clazz).list();
	}

	public List<Task> findSignAUPTasks() {

		return findConcrete(SignAUPTask.class);
	}

	public void removeAllTasks() {

		List<Task> tasks = findAll();

		for (Task t : tasks)
			makeTransient(t);

	}

	public SignAUPTask createSignAUPTask(AUP aup) {

		int lifetime = VOMSConfiguration.instance().getInt(
				VOMSConfiguration.SIGN_AUP_TASK_LIFETIME, 1);
		Calendar cal = Calendar.getInstance();

		// FIXME: change from MINUTE to DAYS when releasing
		cal.add(Calendar.MINUTE, lifetime);

		return createSignAUPTask(aup, cal.getTime());

	}

	public List<Task> getActiveTasks() {

		return findByCriteria(Restrictions.ne("status", TaskStatus.COMPLETED),
				Restrictions.ne("status", TaskStatus.EXPIRED));

	}

}
