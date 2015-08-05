package org.glite.security.voms.admin.view.actions.audit;

import java.util.List;

import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class AuditLogSearchResults {

  final AuditLogSearchParams searchParams;

  final List<AuditEvent> results;

  public AuditLogSearchResults(AuditLogSearchParams sp, List<AuditEvent> results) {

    this.searchParams = sp;
    this.results = results;
  }

  public AuditLogSearchParams getSearchParams() {

    return searchParams;
  }

  public List<AuditEvent> getResults() {

    return results;
  }

}
