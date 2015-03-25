package org.glite.security.voms.admin.event;

import java.security.Principal;
import java.util.Date;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public abstract class AuditableEvent extends AbstractEvent {

  private volatile AuditEvent auditEvent;

  private Principal principal;

  protected AuditableEvent(EventCategory type) {

    super(type);

  }

  private AuditEvent buildAuditEvent() {

    if (auditEvent == null) {

      Validate.notNull(principal);
      Validate.notEmpty(principal.getName());

      auditEvent = new AuditEvent();
      auditEvent.setPrincipal(principal.getName());

      auditEvent.setTimestamp(new Date());
      auditEvent.setType(getClass().getName());

      decorateAuditEvent(auditEvent);
    }

    return auditEvent;
  }

  protected abstract void decorateAuditEvent(AuditEvent e);

  public AuditEvent getAuditEvent() {

    buildAuditEvent();

    return auditEvent;
  }

  public Principal getPrincipal() {

    return principal;
  }

  public void setPrincipal(Principal principal) {

    this.principal = principal;
  }

}
