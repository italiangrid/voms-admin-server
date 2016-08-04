package org.glite.security.voms.admin.persistence.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.persistence.model.audit.AuditEventData;
import org.glite.security.voms.admin.view.actions.audit.AuditLogSearchParams;
import org.glite.security.voms.admin.view.actions.audit.util.AdminNameFormatter;
import org.glite.security.voms.admin.view.actions.audit.util.AnnotationEventMessageBuilder;
import org.glite.security.voms.admin.view.actions.audit.util.EventNameFormatter;
import org.glite.security.voms.admin.view.actions.audit.util.SimpleAdminNameFormatter;
import org.glite.security.voms.admin.view.actions.audit.util.SimpleEventNameFormatter;

public abstract class AbstractAuditLogSearchCommand
  implements AuditLogCtlCommand {

  private static final AnnotationEventMessageBuilder MESSAGE_BUILDER = new AnnotationEventMessageBuilder();
  private static final AdminNameFormatter ADMIN_NAME_FORMATTER = new SimpleAdminNameFormatter();
  private static final EventNameFormatter EVENT_NAME_FORMATTER = new SimpleEventNameFormatter();
  private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");

  
  protected Date parseDateFromString(String s){
    try {
      
      return DATE_FORMATTER.parse(s);
    
    } catch (ParseException e) {
      throw new AuditLogCommandError(e);
    }
  }
  
  
  protected AuditLogSearchParams addConstraintsFromCommandLine(
    CommandLine line) {

    AuditLogSearchParams params = new AuditLogSearchParams();
    
    if (line.hasOption("fromTime")){
      Date fromTime = parseDateFromString(line.getOptionValue("fromTime"));
      params.setFromTime(fromTime);
    }
    
    if (line.hasOption("toTime")){
      Date toTime = parseDateFromString(line.getOptionValue("toTime"));
      params.setToTime(toTime);
    }

    if (line.hasOption("first")) {
      params.setFirstResult(Integer.parseInt(line.getOptionValue("first")));
    }

    if (line.hasOption("maxResults")) {
      params.setMaxResults(Integer.parseInt(line.getOptionValue("maxResults")));
    }

    if (line.hasOption("eventType")) {
      params.setFilterType("type");
      params.setFilterString(line.getOptionValue("eventType"));
    } else if (line.hasOption("principal")) {
      params.setFilterType("principal");
      params.setFilterString(line.getOptionValue("principal"));
    }

    return params;
  }

  protected void printAuditLogEvent(AuditEvent ev) {

    StringBuilder dataString = new StringBuilder();
    int dataCount = 0;

    for (AuditEventData ed : ev.getSortedData()) {

      if (dataCount++ > 0) {
        dataString.append(",");
      }

      String data = String.format("%s = '%s'", ed.getName(),
        ed.getValue());
      dataString.append(data);
    }

    String eventDescription = MESSAGE_BUILDER.buildEventDescription(ev);

    String msg = String.format("%s (%d) %s - '%s' %s - Data:[%s]",
      DATE_FORMATTER.format(ev.getTimestamp()),
      ev.getId(),
      EVENT_NAME_FORMATTER.formatEventName(ev.getType()),
      ADMIN_NAME_FORMATTER.formatAdminName(ev.getPrincipal()), eventDescription,
      dataString.toString());

    System.out.println(msg);

  }

}
