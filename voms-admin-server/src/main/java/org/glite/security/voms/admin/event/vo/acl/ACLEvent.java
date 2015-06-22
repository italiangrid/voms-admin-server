package org.glite.security.voms.admin.event.vo.acl;

import java.util.Map;

import org.glite.security.voms.admin.event.vo.VOEvent;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public class ACLEvent extends VOEvent<ACL> {

  public ACLEvent(ACL acl) {

    super(acl);

  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    ACL acl = getPayload();

    e.addDataPoint("aclContext", acl.getContext().toString());
    e.addDataPoint("aclIsDefault", Boolean.toString(acl.isDefautlACL()));

    int counter = 0;

    for (Map.Entry<VOMSAdmin, VOMSPermission> perm : acl.getPermissions()
      .entrySet()) {

      String permissionString = String.format("%s : %s", perm.getKey()
        .toString(), perm.getValue().toString());

      e.addDataPoint(String.format("aclPermission%d", counter++),
        permissionString);
    }
  }

}
