package org.glite.security.voms.admin.event.vo.role;

import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSRoleAttribute;

public class RoleAttributeDeletedEvent extends RoleAttributeEvent {

  public RoleAttributeDeletedEvent(VOMSRole r, VOMSRoleAttribute roleAttribute) {

    super(r, roleAttribute);

  }

}
