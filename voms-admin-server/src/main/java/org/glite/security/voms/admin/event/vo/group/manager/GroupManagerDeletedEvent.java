package org.glite.security.voms.admin.event.vo.group.manager;

import org.glite.security.voms.admin.persistence.model.GroupManager;

public class GroupManagerDeletedEvent extends GroupManagerEvent {

  public GroupManagerDeletedEvent(GroupManager manager) {

    super(manager);

  }

}
