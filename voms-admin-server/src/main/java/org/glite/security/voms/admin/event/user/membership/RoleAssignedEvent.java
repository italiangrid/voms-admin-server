package org.glite.security.voms.admin.event.user.membership;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

@EventDescription(message = "assigned role '%s' in group '%s' to user '%s %s'",
params = { "roleName", "groupName", "userName", "userSurname" })
public class RoleAssignedEvent extends UserMembershipEvent {

  public RoleAssignedEvent(VOMSUser payload, VOMSGroup group, VOMSRole r) {

    super(payload, group, r);

  }

}
