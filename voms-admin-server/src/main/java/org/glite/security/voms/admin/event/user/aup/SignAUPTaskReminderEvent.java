package org.glite.security.voms.admin.event.user.aup;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;

@EventDescription(message = "sent a sign AUP reminder to user '%s %s'",
params = { "userName", "userSurname" })
public class SignAUPTaskReminderEvent extends UserAUPTaskEvent {

  public SignAUPTaskReminderEvent(VOMSUser user, AUP aup, SignAUPTask task) {

    super(user, aup, task);
  }

}
