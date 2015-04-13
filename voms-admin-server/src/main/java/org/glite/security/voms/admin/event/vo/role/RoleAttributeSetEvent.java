package org.glite.security.voms.admin.event.vo.role;

import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSRoleAttribute;

public class RoleAttributeSetEvent extends RoleAttributeEvent {

  public RoleAttributeSetEvent(VOMSRole r, VOMSRoleAttribute roleAttribute) {

    super(r, roleAttribute);
  }

}
