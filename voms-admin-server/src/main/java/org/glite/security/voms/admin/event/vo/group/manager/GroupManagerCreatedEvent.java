package org.glite.security.voms.admin.event.vo.group.manager;

import org.glite.security.voms.admin.persistence.model.GroupManager;

public class GroupManagerCreatedEvent extends GroupManagerEvent {

  public GroupManagerCreatedEvent(GroupManager manager) {

    super(manager);

  }

}
