package org.glite.security.voms.admin.operations.audit;

import java.util.List;
import java.util.concurrent.Callable;

import org.glite.security.voms.admin.persistence.dao.generic.AuditSearchDAO;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class FindLatestKAuditLogEvents implements Callable<List<AuditEvent>> {

  final int numEvents;
  final AuditSearchDAO dao;

  public FindLatestKAuditLogEvents(int numEvents, AuditSearchDAO dao) {

    this.numEvents = numEvents;
    this.dao = dao;
  }

  @Override
  public List<AuditEvent> call() throws Exception {

    return dao.findLatestEvents(numEvents);

  }

}
