package org.glite.security.voms.admin.event.vo.role;

import org.glite.security.voms.admin.persistence.model.VOMSRole;

public class RoleDeletedEvent extends RoleEvent {

  public RoleDeletedEvent(VOMSRole r) {

    super(r);

  }

}
