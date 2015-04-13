package org.glite.security.voms.admin.event.user.membership;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class RoleDismissedEvent extends UserMembershipEvent {

  public RoleDismissedEvent(VOMSUser payload, VOMSGroup group, VOMSRole r) {

    super(payload, group, r);

  }

}
