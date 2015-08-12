package org.glite.security.voms.admin.event.vo;

import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.SinglePayloadAuditableEvent;

public abstract class VOEvent<T> extends SinglePayloadAuditableEvent<T> {

  protected VOEvent(T payload) {

    super(EventCategory.VOLifecycleEvent, payload);

  }

}
