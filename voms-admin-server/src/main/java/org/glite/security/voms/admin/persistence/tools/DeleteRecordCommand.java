package org.glite.security.voms.admin.persistence.tools;

import org.apache.commons.cli.CommandLine;
import org.glite.security.voms.admin.persistence.dao.generic.AuditDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class DeleteRecordCommand extends AbstractAuditLogSearchCommand {

  public static final String NAME = "delete-record";

  @Override
  public void execute(CommandLine line) {

    if (!line.hasOption("id")) {
      throw new AuditLogCommandError(
        "Please provide a record id (using the id option)");
    }

    Long id = Long.parseLong(line.getOptionValue("id"));
    AuditDAO dao = DAOFactory.instance().getAuditDAO();
    AuditEvent event = dao.findById(id, true);
    System.out.println("Deleting the following audit event...");
    
    printAuditLogEvent(event);
  
    System.out.println("Done.");
    dao.makeTransient(event);

  }

  @Override
  public String getDescription() {
    return "Deletes the record from the audit log identified by the --id option";
  }
  
  @Override
  public String getName() {
  
   return NAME;
  }

}
