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

    String fetchQuery = "from Notification n where n.status = 'IN_DELIVERY' and n.handlerId = :handlerId";

    Query qq = getSession().createQuery(fetchQuery)
      .setString("handlerId", handlerId)
      .setMaxResults(numJobs)
      .setLockMode("n", LockMode.UPGRADE);

    return qq.list();
  }

  @Override
  public int markJobs(int numJobs, String handlerId) {

    Validate.notNull(handlerId);
    Validate.isTrue(numJobs > 0, "numJobs must be a positive integer");

    String updateQ = "update Notification set status = 'IN_DELIVERY', handlerId = :handlerId where status = 'QUEUED' and handlerId is null";

    Query q = getSession().createQuery(updateQ).setString("handlerId",
      handlerId);

    return q.executeUpdate();
  }

}
