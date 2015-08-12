package org.glite.security.voms.admin.event.user.aup;

import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;

public class SignAUPTaskReminderEvent extends UserAUPTaskEvent {

  public SignAUPTaskReminderEvent(VOMSUser user, AUP aup, SignAUPTask task) {

    super(user, aup, task);
  }

}
