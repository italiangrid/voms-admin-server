package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

@EventDescription(message = "updated user '%s %s' personal information",
  params = { "userName", "userSurname" })
public class UserPersonalInformationUpdatedEvent extends UserLifecycleEvent {

  public UserPersonalInformationUpdatedEvent(VOMSUser payload) {

    super(payload);

  }

}
