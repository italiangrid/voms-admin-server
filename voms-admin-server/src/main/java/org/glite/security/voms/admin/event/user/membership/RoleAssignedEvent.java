package org.glite.security.voms.admin.event.user.membership;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;


public class RoleAssignedEvent extends UserMembershipEvent {

  public RoleAssignedEvent(VOMSUser payload, VOMSGroup group, VOMSRole r) {

    super(payload, group, r);

  }

}
