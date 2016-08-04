package org.glite.security.voms.admin.persistence.tools;

import org.apache.commons.cli.CommandLine;

public interface AuditLogCtlCommand {

  void execute(CommandLine line);
  String getDescription();
  String getName();
  
}
