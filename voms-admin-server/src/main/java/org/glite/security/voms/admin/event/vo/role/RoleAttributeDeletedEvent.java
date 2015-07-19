package org.glite.security.voms.admin.event.vo.role;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSRoleAttribute;

@EventDescription(message="deleted attribute '%s' for role '%s'", params={"roleAttributeName", "roleName"})
public class RoleAttributeDeletedEvent extends RoleAttributeEvent {

  public RoleAttributeDeletedEvent(VOMSRole r, VOMSRoleAttribute roleAttribute) {

    super(r, roleAttribute);

  }

}
