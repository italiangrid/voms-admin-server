package org.glite.security.voms.admin.event.vo.group.manager;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.GroupManager;
@EventDescription(message="created group manager '%s' for group %s", params={"groupManagerName", "groupName"})
public class GroupManagerCreatedEvent extends GroupManagerEvent {

  public GroupManagerCreatedEvent(GroupManager manager) {

    super(manager);

  }

}
