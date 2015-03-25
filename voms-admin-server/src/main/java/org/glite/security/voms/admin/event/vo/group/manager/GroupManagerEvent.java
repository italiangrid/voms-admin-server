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
