package org.glite.security.voms.admin.view.actions.audit;

import org.hibernate.ScrollableResults;

public class ScrollableAuditLogSearchResults {

  final AuditLogSearchParams searchParams;
  final ScrollableResults results;

  public ScrollableAuditLogSearchResults(AuditLogSearchParams sp,
    ScrollableResults results) {
    this.searchParams = sp;
    this.results = results;
  }

  public ScrollableResults getResults() {

    return results;
  }

  public AuditLogSearchParams getSearchParams() {

    return searchParams;
  }

}
