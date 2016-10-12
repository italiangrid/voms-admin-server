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
