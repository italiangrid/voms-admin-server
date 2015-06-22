package org.glite.security.voms.admin.event.auditing;

import java.util.EnumSet;

import org.apache.commons.lang.xwork.Validate;
import org.glite.security.voms.admin.event.AuditableEvent;
import org.glite.security.voms.admin.event.Event;
import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.EventListener;
import org.glite.security.voms.admin.persistence.dao.generic.AuditDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum AuditLog implements EventListener {

  INSTANCE;

  public static final Logger LOG = LoggerFactory.getLogger(AuditLog.class);

  private PrincipalProvider principalProvider;
  private DAOFactory daoFactory;

  private AuditLog() {

  }

  @Override
  public EnumSet<EventCategory> getCategoryMask() {

    return EventCategory.ALL_CATEGORIES;
  }

  @Override
  public void fire(Event e) {

    Validate.notNull(principalProvider,
      "AuditLog is not properly initialized. Please set a principalProvider");
    Validate.notNull(daoFactory,
      "AuditLog is not properly initialized. Please set a DAO Factory");

    Validate.notNull(e);

    if (!(e instanceof AuditableEvent)) {
      LOG.debug("Event {} was not persisted as not of type AuditableEvent", e);
      return;
    }

    AuditDAO dao = daoFactory.getAuditDAO();

    AuditableEvent ee = (AuditableEvent) e;

    ee.setPrincipal(principalProvider.getCurrentPrincipal());

    AuditEvent ae = ee.getAuditEvent();

    LOG.debug("Saving audit event: {}", ae);

    dao.makePersistent(ae);

  }

  public PrincipalProvider getPrincipalProvider() {

    return principalProvider;
  }

  public void setPrincipalProvider(PrincipalProvider principalProvider) {

    this.principalProvider = principalProvider;
  }

  public DAOFactory getDaoFactory() {

    return daoFactory;
  }

  public void setDaoFactory(DAOFactory daoFactory) {

    this.daoFactory = daoFactory;
  }

}
