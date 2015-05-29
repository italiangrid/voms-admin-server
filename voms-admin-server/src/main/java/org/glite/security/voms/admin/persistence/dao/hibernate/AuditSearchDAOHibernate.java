package org.glite.security.voms.admin.persistence.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.persistence.dao.generic.AuditSearchDAO;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchParams;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchResults;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class AuditSearchDAOHibernate extends
  GenericHibernateDAO<AuditEvent, Long> implements AuditSearchDAO {

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

  @SuppressWarnings("unchecked")
  @Override
  public AuditLogSearchResults findEventsMatchingParams(AuditLogSearchParams sp) {

    Criteria crit = createCriteria();

    if (sp.getFromTime() != null) {
      crit.add(Restrictions.ge("timestamp", sp.getFromTime()));
    }

    if (sp.getToTime() != null) {
      crit.add(Restrictions.le("timestamp", sp.getToTime()));
    }

    if (sp.getFilterString() != null && !sp.getFilterString().trim().equals("")) {
      crit.add(Restrictions.like(sp.getFilterType(), sp.getFilterString()
        .trim()));
    }

    crit.addOrder(Order.desc("timestamp"));
    List<AuditEvent> results = crit.list();

    return new AuditLogSearchResults(sp, results);
  }

}
