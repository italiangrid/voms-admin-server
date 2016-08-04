package org.glite.security.voms.admin.persistence.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.cli.CommandLine;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.generic.AuditSearchDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchParams;
import org.glite.security.voms.admin.view.actions.audit.ScrollableAuditLogSearchResults;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

public class DeleteRecordsCommand extends AbstractAuditLogSearchCommand {

  public static final String NAME = "delete-records";

  private boolean getConfirmationFromUser() throws IOException {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String line = br.readLine();

    if (line.trim().equalsIgnoreCase("yes")) {
      return true;
    }

    return false;

  }

  @Override
  public void execute(CommandLine line) {

    AuditSearchDAO dao = DAOFactory.instance().getAuditSearchDAO();
    AuditLogSearchParams params = addConstraintsFromCommandLine(line);

    ScrollableAuditLogSearchResults results = dao
      .scrollEventsMatchingParams(params, ScrollMode.SCROLL_INSENSITIVE);

    int count = 0;
    ScrollableResults scroll = results.getResults();

    scroll.last();
    count = scroll.getRowNumber();

    if (count + 1 == 0) {
      System.out.println("No events found");
      return;
    }

    System.out.format(
      "%d records will be deleted. Type 'yes' if you want to continue\n",
      count + 1);

    try {
      if (!getConfirmationFromUser()) {
        System.out.println("Aborting on user's request.");
        return;
      }
    } catch (IOException e) {
      throw new AuditLogCommandError(e);
    }

    scroll.beforeFirst();
    System.out.println("Deleting events:");

    count = 0;

    while (scroll.next()) {
      count++;
      AuditEvent event = (AuditEvent) scroll.get()[0];

      printAuditLogEvent(event);
      dao.makeTransient(event);
      dao.flush();
      HibernateFactory.getSession().evict(event);
    }

    System.out.format("%d events deleted.\n", count);

  }

  @Override
  public String getDescription() {

    return "Deletes records from the audit log matching the search parameters. "
      + "If no parameters are provided, deletes ALL records.";
  }

  @Override
  public String getName() {

    return NAME;
  }

}
