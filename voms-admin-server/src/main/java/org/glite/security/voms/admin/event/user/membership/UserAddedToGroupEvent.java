package org.glite.security.voms.admin.event.user.membership;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSUser;


public class UserAddedToGroupEvent extends UserMembershipEvent {
  
  public UserAddedToGroupEvent(VOMSUser u, VOMSGroup g) {

    super(u, g, null);
    
  }

}
