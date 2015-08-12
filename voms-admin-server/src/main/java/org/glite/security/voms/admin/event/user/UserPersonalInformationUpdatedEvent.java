package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class UserPersonalInformationUpdatedEvent extends UserLifecycleEvent {

  public UserPersonalInformationUpdatedEvent(VOMSUser payload) {

    super(payload);

  }

}
