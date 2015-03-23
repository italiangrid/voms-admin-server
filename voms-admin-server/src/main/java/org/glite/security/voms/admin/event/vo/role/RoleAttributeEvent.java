package org.glite.security.voms.admin.event.vo.role;

import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSRoleAttribute;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class RoleAttributeEvent extends RoleEvent {

  final VOMSRoleAttribute roleAttribute;

  public RoleAttributeEvent(VOMSRole r, VOMSRoleAttribute roleAttribute) {

    super(r);
    this.roleAttribute = roleAttribute;
  }

  public VOMSRoleAttribute getRoleAttribute() {

    return roleAttribute;
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    super.decorateAuditEvent(e);

    e.addDataPoint("roleAttributeContext", roleAttribute.getContext());
    e.addDataPoint("roleAttributeName", roleAttribute.getName());
    e.addDataPoint("roleAttributeValue", roleAttribute.getValue());

  }
}
