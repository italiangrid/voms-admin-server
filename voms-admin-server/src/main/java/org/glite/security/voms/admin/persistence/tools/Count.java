package org.glite.security.voms.admin.persistence.tools;

import org.apache.commons.cli.CommandLine;
import org.glite.security.voms.admin.persistence.dao.generic.AuditSearchDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchParams;

public class Count extends AbstractAuditLogSearchCommand {

  public static final String NAME = "count";

  @Override
  public void execute(CommandLine line) {

    AuditSearchDAO dao = DAOFactory.instance().getAuditSearchDAO();

    AuditLogSearchParams params = addConstraintsFromCommandLine(line);

    System.out.println(dao.countEventsMatchingParams(params));
  }

  @Override
  public String getDescription() {

    return "Count records in the audit log matching the search parameters. "
      + "If no parameters are provided, counts all records.";

  }

  @Override
  public String getName() {
    return NAME;
  }

}
