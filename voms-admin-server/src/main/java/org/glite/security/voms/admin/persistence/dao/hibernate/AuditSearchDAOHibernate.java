/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
import org.glite.security.voms.admin.persistence.dao.generic.AuditSearchDAO;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchParams;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchResults;
import org.glite.security.voms.admin.view.actions.audit.ScrollableAuditLogSearchResults;
import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class AuditSearchDAOHibernate
  extends GenericHibernateDAO<AuditEvent, Long> implements AuditSearchDAO {

  AuditSearchDAOHibernate() {

  }

  @SuppressWarnings("unchecked")
  @Override
  public List<AuditEvent> findLatestEvents(int numEvents) {

    Validate.isTrue(numEvents >= 0, "numEvents must be a positive integer.");

    Criteria crit = createCriteria();
    crit.addOrder(Order.desc("timestamp"));
    crit.setMaxResults(numEvents);

    return crit.list();
  }

  @Override
  public Integer countEvents() {

    Criteria crit = createCriteria();
    crit.setProjection(Projections.rowCount());
    return (Integer) crit.uniqueResult();
  }

  protected Criteria buildCriteriaFromParams(AuditLogSearchParams sp) {

    Criteria crit = createCriteria();

    crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

    if (sp.getFromTime() != null) {
      crit.add(Restrictions.ge("timestamp", sp.getFromTime()));
    }

    if (sp.getToTime() != null) {
      crit.add(Restrictions.le("timestamp", sp.getToTime()));
    }

    if (sp.getFilterString() != null && !sp.getFilterString()
      .trim()
      .equals("")) {
      crit.add(Restrictions.like(sp.getFilterType(), sp.getFilterString()
        .trim(), MatchMode.ANYWHERE));
    }

    if (sp.getFirstResult() != null) {
      crit.setFirstResult(sp.getFirstResult());
    }

    if (sp.getMaxResults() != null) {
      crit.setMaxResults(sp.getMaxResults());
    }

    crit.addOrder(Order.desc("timestamp"));
    return crit;
  }

  @SuppressWarnings("unchecked")
  @Override
  public AuditLogSearchResults findEventsMatchingParams(
    AuditLogSearchParams sp) {

    Criteria crit = buildCriteriaFromParams(sp);
    List<AuditEvent> results = crit.list();

    crit.setProjection(Projections.rowCount());

    // This is a workaround for
    // https://hibernate.atlassian.net/browse/HHH-4761
    // TLDR:
    // The count(*) project doesn't work well with
    // the limit implementation of the MySQL Hibernate dialect.
    // Since what we need here is a way to count all results
    // matching the query in a way that is not relative to the page
    // size, we can ignore the pagination limits and just set
    // arbirtray values and the count(*) result will be accurate
    crit.setFirstResult(0);
    crit.setMaxResults(100);

    Integer count = (Integer) crit.uniqueResult();

    if (count == null) {
      count = 0;
    }

    return new AuditLogSearchResults(sp, count, results);
  }

  public ScrollableAuditLogSearchResults scrollEventsMatchingParams(
    AuditLogSearchParams sp, ScrollMode mode) {

    Criteria crit = buildCriteriaFromParams(sp);
    ScrollableResults results = crit.scroll(mode);

    return new ScrollableAuditLogSearchResults(sp, results);

  }

  @Override
  public Integer countEventsMatchingParams(AuditLogSearchParams sp) {

    Criteria crit = buildCriteriaFromParams(sp);
    crit.setProjection(Projections.rowCount());

    // This is a workaround for
    // https://hibernate.atlassian.net/browse/HHH-4761
    // TLDR:
    // The count(*) project doesn't work well with
    // the limit implementation of the MySQL Hibernate dialect.
    // Since what we need here is a way to count all results
    // matching the query in a way that is not relative to the page
    // size, we can ignore the pagination limits and just set
    // arbirtray values and the count(*) result will be accurate
    crit.setFirstResult(0);
    crit.setMaxResults(100);

    return (Integer) crit.uniqueResult();
  }

}
