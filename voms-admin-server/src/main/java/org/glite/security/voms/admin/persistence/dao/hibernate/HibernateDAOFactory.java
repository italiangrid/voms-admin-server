/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.persistence.dao.hibernate;

import java.util.List;

import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AUPVersionDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AuditDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.GroupDAO;
import org.glite.security.voms.admin.persistence.dao.generic.GroupManagerDAO;
import org.glite.security.voms.admin.persistence.dao.generic.PeriodicNotificationsDAO;
import org.glite.security.voms.admin.persistence.dao.generic.RequestDAO;
import org.glite.security.voms.admin.persistence.dao.generic.RequesterInfoDAO;
import org.glite.security.voms.admin.persistence.dao.generic.TaskDAO;
import org.glite.security.voms.admin.persistence.dao.generic.TaskLogRecordDAO;
import org.glite.security.voms.admin.persistence.dao.generic.TaskTypeDAO;
import org.glite.security.voms.admin.persistence.dao.generic.UserDAO;
import org.glite.security.voms.admin.persistence.model.AUPVersion;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.persistence.model.request.RequesterInfo;
import org.glite.security.voms.admin.persistence.model.task.LogRecord;
import org.glite.security.voms.admin.persistence.model.task.Task;
import org.glite.security.voms.admin.persistence.model.task.TaskType;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  public static class AuditDAOHibernate extends
    GenericHibernateDAO<AuditEvent, Long> implements AuditDAO {

  }

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
    GenericHibernateDAO<RequesterInfo, Long> implements RequesterInfoDAO {

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

  private static Logger log = LoggerFactory
    .getLogger(HibernateDAOFactory.class);

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

  public PeriodicNotificationsDAO getPeriodicNotificationsDAO() {

    return (PeriodicNotificationsDAO) instantiateDAO(PeriodicNotificationDAOHibernate.class);
  }

  private GenericHibernateDAO instantiateDAO(Class daoClass) {

    try {
      log.debug("Instantiating DAO: " + daoClass);
      return (GenericHibernateDAO) daoClass.newInstance();
    } catch (Exception ex) {
      throw new RuntimeException("Can not instantiate DAO: " + daoClass, ex);
    }
  }

  @Override
  public GroupManagerDAO getGroupManagerDAO() {

    return (GroupManagerDAO) instantiateDAO(GroupManagerDAOHibernate.class);

  }

  @Override
  public AuditDAO getAuditDAO() {

    return (AuditDAO) instantiateDAO(AuditDAOHibernate.class);
  }
}
