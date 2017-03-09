package org.glite.security.voms.admin.event.user.aup;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;

@EventDescription(message="canceled sign aup task for user %s,%s: %s",
params = { "userName", "userSurname", "taskCancellationReason" })
public class SignAUPTaskCanceledEvent extends UserAUPTaskEvent{
  
  private final String cancellationReason;
  
  public SignAUPTaskCanceledEvent(SignAUPTask task, String cancellationReason) {
   super(task.getUser(), task.getAup(), task);
   this.cancellationReason = cancellationReason;
  }
  
  @Override
  protected void decorateAuditEvent(AuditEvent e) {
    super.decorateAuditEvent(e);
    e.addDataPoint("taskCancellationReason", cancellationReason);
  }
}
