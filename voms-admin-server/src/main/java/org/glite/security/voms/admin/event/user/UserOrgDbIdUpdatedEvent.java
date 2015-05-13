package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.persistence.model.VOMSUser;


public class UserOrgDbIdUpdatedEvent extends UserLifecycleEvent {

  public UserOrgDbIdUpdatedEvent(VOMSUser payload) {

    super(payload);
    
  }
}
