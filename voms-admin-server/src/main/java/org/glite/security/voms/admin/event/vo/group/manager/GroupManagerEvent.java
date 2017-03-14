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
package org.glite.security.voms.admin.event.vo.group.manager;

import org.glite.security.voms.admin.event.vo.VOEvent;
import org.glite.security.voms.admin.persistence.model.GroupManager;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.audit.AuditEvent;

public abstract class GroupManagerEvent extends VOEvent<GroupManager> {

  public GroupManagerEvent(GroupManager manager) {

    super(manager);

  }

  @Override
  protected void decorateAuditEvent(AuditEvent e) {

    e.addDataPoint("groupManagerName", getPayload().getName());
    e.addDataPoint("groupManagerDescription", getPayload().getDescription());
    e.addDataPoint("groupManagerEmailAddress", getPayload().getEmailAddress());

    int managedGroupCounter = 0;

    for (VOMSGroup g : getPayload().getManagedGroups()) {

      String managedGroupLabel = String.format("managedGroup%d",
        managedGroupCounter++);

      e.addDataPoint(managedGroupLabel, g.getName());
    }

  }
}
