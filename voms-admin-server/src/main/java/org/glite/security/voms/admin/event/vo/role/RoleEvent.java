package org.glite.security.voms.admin.event.vo.role;

import org.apache.commons.lang.xwork.Validate;
import org.glite.security.voms.admin.event.vo.VOEvent;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class RoleEvent extends VOEvent<VOMSRole> {

  public static final String ROLE_NAME = "roleName";

  protected RoleEvent(VOMSRole r) {

    super(r);
    Validate.notNull(r);
  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    e.addDataPoint(ROLE_NAME, getPayload().getName());

  }
}
