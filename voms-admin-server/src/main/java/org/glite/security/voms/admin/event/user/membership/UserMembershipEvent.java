/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.event.user.membership;

import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.event.user.UserEvent;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public abstract class UserMembershipEvent extends UserEvent {

  final VOMSGroup group;
  final VOMSRole role;

  protected UserMembershipEvent(VOMSUser payload, VOMSGroup group, VOMSRole r) {

    super(EventCategory.UserMembershipEvent, payload);
    this.group = group;
    this.role = r;

  }
  
  @Override
  protected void decorateAuditEvent(AuditEvent e) {
  
    super.decorateAuditEvent(e);
    e.addDataPoint("groupName", group.getName());
    
    if (role != null){
      e.addDataPoint("roleName", role.getName());
    }
  }

}
