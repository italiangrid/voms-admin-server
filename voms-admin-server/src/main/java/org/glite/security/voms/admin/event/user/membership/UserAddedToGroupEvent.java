package org.glite.security.voms.admin.event.user.membership;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

@EventDescription(message = "added user '%s %s' to group '%s'", params = {
  "userName", "userSurname", "groupName" })
public class UserAddedToGroupEvent extends UserMembershipEvent {

  public UserAddedToGroupEvent(VOMSUser u, VOMSGroup g) {

    super(u, g, null);

  }

}
