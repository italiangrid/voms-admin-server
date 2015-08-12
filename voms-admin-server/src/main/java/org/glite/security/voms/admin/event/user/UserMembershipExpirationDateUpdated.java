package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.persistence.model.VOMSUser;


public class UserMembershipExpirationDateUpdated extends UserLifecycleEvent {

  public UserMembershipExpirationDateUpdated(VOMSUser payload) {
    super(payload);
  }

}
