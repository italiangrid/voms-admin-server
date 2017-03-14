/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
