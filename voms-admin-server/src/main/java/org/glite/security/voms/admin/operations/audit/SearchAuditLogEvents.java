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
