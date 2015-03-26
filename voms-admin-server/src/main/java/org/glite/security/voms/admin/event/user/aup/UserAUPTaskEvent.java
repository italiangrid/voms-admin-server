package org.glite.security.voms.admin.event.user.aup;

import static org.glite.security.voms.admin.event.auditing.NullHelper.nullSafeValue;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;

public class UserAUPTaskEvent extends UserAUPEvent {

  final SignAUPTask task;

  public UserAUPTaskEvent(VOMSUser user, AUP aup, SignAUPTask task) {

    super(user, aup);
    Validate.notNull(task);
    this.task = task;
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    super.decorateAuditEvent(e);
    e.addDataPoint("taskExpirationDate", nullSafeValue(task.getExpiryDate()));
    e.addDataPoint("taskLastNotificationTime",
      nullSafeValue(task.getLastNotificationTime()));

  }

  public SignAUPTask getTask() {

    return task;
  }
}
