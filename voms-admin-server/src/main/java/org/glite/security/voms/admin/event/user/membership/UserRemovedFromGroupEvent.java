package org.glite.security.voms.admin.event.user.membership;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

@EventDescription(message = "removed user '%s %s' from group '%s'", params = {
  "userName", "userSurname", "groupName" })
public class UserRemovedFromGroupEvent extends UserMembershipEvent {

  public UserRemovedFromGroupEvent(VOMSUser u, VOMSGroup g) {

    super(u, g, null);

  }

}
