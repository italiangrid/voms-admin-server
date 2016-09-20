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
