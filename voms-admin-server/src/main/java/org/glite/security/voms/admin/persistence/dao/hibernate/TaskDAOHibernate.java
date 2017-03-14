/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
package org.glite.security.voms.admin.persistence.dao.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskDAO;
import org.glite.security.voms.admin.persistence.dao.generic.TaskTypeDAO;
import org.glite.security.voms.admin.persistence.error.VOMSDatabaseException;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.request.Request;
import org.glite.security.voms.admin.persistence.model.task.ApproveUserRequestTask;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.glite.security.voms.admin.persistence.model.task.Task;
import org.glite.security.voms.admin.persistence.model.task.Task.TaskStatus;
import org.glite.security.voms.admin.persistence.model.task.TaskType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskDAOHibernate extends GenericHibernateDAO<Task, Long> implements
  TaskDAO {

  private static final Logger log = LoggerFactory
    .getLogger(TaskDAOHibernate.class);

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
      VOMSConfigurationConstants.SIGN_AUP_TASK_LIFETIME, 15);

    Calendar cal = Calendar.getInstance();

    // Negative lifetime values are useful for debugging purposes!
    if (lifetime > 0) {
      cal.add(Calendar.DAY_OF_YEAR, lifetime);
    }

    log.debug("Scheduling SignAUP task with expiration date: {}", cal.getTime()
      .toString());

    return createSignAUPTask(aup, cal.getTime());

  }

  public List<Task> getActiveTasks() {

    return findByCriteria(Restrictions.ne("status", TaskStatus.COMPLETED),
      Restrictions.ne("status", TaskStatus.EXPIRED));

  }

  @Override
  public List<SignAUPTask> findActiveSignAUPTasks() {

    Criteria crit = getSession()
      .createCriteria(SignAUPTask.class);
    
    crit.add(Restrictions.eq("status", TaskStatus.CREATED));
    
    return crit.list();
  }


}
