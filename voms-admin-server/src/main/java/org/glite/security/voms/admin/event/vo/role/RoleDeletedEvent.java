package org.glite.security.voms.admin.event.vo.role;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSRole;

@EventDescription(message="deleted role '%s'", params="roleName")
public class RoleDeletedEvent extends RoleEvent {

  public RoleDeletedEvent(VOMSRole r) {

    super(r);

  }

}
