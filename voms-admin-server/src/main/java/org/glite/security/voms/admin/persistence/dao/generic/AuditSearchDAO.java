package org.glite.security.voms.admin.persistence.dao.generic;

import java.util.List;

import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchParams;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchResults;

public interface AuditSearchDAO extends GenericDAO<AuditEvent, Long> {

  List<AuditEvent> findLatestEvents(int numEvents);
  Integer countEvents();
  
  AuditLogSearchResults findEventsMatchingParams(AuditLogSearchParams sp);
   
}
