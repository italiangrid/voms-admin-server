package org.glite.security.voms.admin.operations.audit;

import java.util.concurrent.Callable;

import org.glite.security.voms.admin.persistence.dao.generic.AuditSearchDAO;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchParams;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchResults;

public class SearchAuditLogEvents implements Callable<AuditLogSearchResults> {

  final AuditLogSearchParams searchParams;
  final AuditSearchDAO dao;

  public SearchAuditLogEvents(AuditLogSearchParams sp, AuditSearchDAO dao) {

    this.searchParams = sp;
    this.dao = dao;
  }

  @Override
  public AuditLogSearchResults call() throws Exception {

    return dao.findEventsMatchingParams(searchParams);

  }

}
