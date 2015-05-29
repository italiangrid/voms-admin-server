package org.glite.security.voms.admin.event.vo.role;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
@EventDescription(message="created role '%s'", params="roleName")
public class RoleCreatedEvent extends RoleEvent {

  public RoleCreatedEvent(VOMSRole r) {

    super(r);

  }

}
