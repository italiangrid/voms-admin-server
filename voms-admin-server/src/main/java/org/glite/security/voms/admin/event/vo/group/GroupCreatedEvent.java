package org.glite.security.voms.admin.event.vo.group;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.event.MainEventDataPoints;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;

@MainEventDataPoints("groupName")
@EventDescription(message="created group '%s'", params="groupName")
public class GroupCreatedEvent extends GroupEvent {

  public GroupCreatedEvent(VOMSGroup g) {

    super(g);
  }

}
