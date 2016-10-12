/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glite.security.voms.admin.view.actions.audit;

import java.util.List;

import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class AuditLogSearchResults {

  final int totalResultsCount;

  final AuditLogSearchParams searchParams;

  final List<AuditEvent> results;

  public AuditLogSearchResults(AuditLogSearchParams sp, int totalResultsCount,
    List<AuditEvent> results) {

    this.searchParams = sp;
    this.totalResultsCount = totalResultsCount;
    this.results = results;
  }

  public AuditLogSearchParams getSearchParams() {

    return searchParams;
  }

  public List<AuditEvent> getResults() {

    return results;
  }

  public int getTotalResultsCount() {

    return totalResultsCount;
  }

  public int getNextPageIndex() {

    return results.size() + searchParams.firstResult;
  }

  public int getPreviousPageIndex() {

    return searchParams.firstResult - searchParams.maxResults;
  }

  public boolean hasNextPage() {

    return (getNextPageIndex() < totalResultsCount);
  }

  public boolean hasPreviousPage() {

    return (getPreviousPageIndex() >= 0);
  }
}
