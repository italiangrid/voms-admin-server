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
package org.glite.security.voms.admin.event.vo.role;

import org.glite.security.voms.admin.event.MainEventDataPoints;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSRoleAttribute;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

@MainEventDataPoints({"roleName", "roleAttributeName", "roleAttributeValue"})
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
