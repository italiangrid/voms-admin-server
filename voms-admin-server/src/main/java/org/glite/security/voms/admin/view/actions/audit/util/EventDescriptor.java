package org.glite.security.voms.admin.view.actions.audit.util;

import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;


public interface EventDescriptor {

  String buildEventDescription(AuditEvent event);
}
