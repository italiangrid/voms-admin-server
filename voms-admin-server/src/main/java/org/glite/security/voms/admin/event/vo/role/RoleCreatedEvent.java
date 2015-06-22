package org.glite.security.voms.admin.event.vo.role;

import org.glite.security.voms.admin.persistence.model.VOMSRole;

public class RoleCreatedEvent extends RoleEvent {

  public RoleCreatedEvent(VOMSRole r) {

    super(r);

  }

}
