package org.glite.security.voms.admin.event.vo.group.manager;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.GroupManager;
@EventDescription(message="removed group manager '%s' from group %s", params={"groupManagerName", "groupName"})
public class GroupManagerDeletedEvent extends GroupManagerEvent {

  public GroupManagerDeletedEvent(GroupManager manager) {

    super(manager);

  }

}
