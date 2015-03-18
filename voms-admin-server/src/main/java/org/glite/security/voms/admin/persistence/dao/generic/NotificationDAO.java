package org.glite.security.voms.admin.persistence.dao.generic;

import java.util.List;

import org.glite.security.voms.admin.persistence.model.notification.Notification;
import org.hibernate.Session;

public interface NotificationDAO extends GenericDAO<Notification, Long> {
  
  public List<Notification> fetchJobs(int numJobs, String handlerId);

  public void setSession(Session session);

}
