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
package org.glite.security.voms.admin.persistence.tools;

import org.apache.commons.cli.CommandLine;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.generic.AuditSearchDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchParams;
import org.glite.security.voms.admin.view.actions.audit.ScrollableAuditLogSearchResults;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

public class Print extends AbstractAuditLogSearchCommand {

  public static final String NAME = "print";
  public static final Integer DEFAULT_MAX_RESULTS = 100;

  @Override
  public void execute(CommandLine line) {

    AuditSearchDAO dao = DAOFactory.instance().getAuditSearchDAO();
    AuditLogSearchParams params = addConstraintsFromCommandLine(line);

    Integer maxResults = params.getMaxResults();
    if (maxResults == null) {
      maxResults = DEFAULT_MAX_RESULTS;
    }

    ScrollableAuditLogSearchResults results = dao
      .scrollEventsMatchingParams(params, ScrollMode.FORWARD_ONLY);

    ScrollableResults scroll = results.getResults();

    int count = 0;

    while (scroll.next()) {
      count++;

      AuditEvent e = (AuditEvent) scroll.get()[0];
      printAuditLogEvent(e);
      HibernateFactory.getSession().evict(e);

    }

    if (count == 0) {
      System.out.println("No events found.");
    } else {
      System.out.format("%d events printed\n", count);
    }

  }

  @Override
  public String getDescription() {

    return "Prints records in the audit LOG matching the search parameters. "
      + "If no parameters are provided, prints all records.";
    
  }

  @Override
  public String getName() {
    return NAME;
  }

}
