package org.glite.security.voms.admin.persistence.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.persistence.dao.generic.NotificationDAO;
import org.glite.security.voms.admin.persistence.model.notification.Notification;
import org.hibernate.LockMode;
import org.hibernate.Query;

public class NotificationDAOHibernate extends
  GenericHibernateDAO<Notification, Long> implements NotificationDAO {

  public NotificationDAOHibernate() {

  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Notification> fetchJobs(int numJobs, String handlerId) {

    Validate.notNull(handlerId);
    Validate.isTrue(numJobs > 0, "numJobs must be a positive integer");

    String fetchQuery = "from Notification n where n.status = 'QUEUED' and n.handlerId is null";

    Query qq = getSession().createQuery(fetchQuery).setMaxResults(numJobs)
      .setLockMode("n", LockMode.UPGRADE);

    return qq.list();
  }

}
