package org.glite.security.voms.admin.event.vo.group;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.event.MainEventDataPoints;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;

@MainEventDataPoints("groupName")
@EventDescription(message="deleted group '%s'", params="groupName")
public class GroupDeletedEvent extends GroupEvent {

  public GroupDeletedEvent(VOMSGroup g) {

    super(g);
  }

}
