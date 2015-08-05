package org.glite.security.voms.admin.event;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public abstract class SinglePayloadAuditableEvent<T> extends AuditableEvent {

  final T payload;

  protected SinglePayloadAuditableEvent(EventCategory type, T payload) {

    super(type);
    Validate.notNull(payload, "Event payload cannot be null.");
    this.payload = payload;

  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

  }

  public T getPayload() {

    return payload;
  }

  
}
