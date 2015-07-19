package org.glite.security.voms.admin.event.vo.group;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.event.MainEventDataPoints;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;

@MainEventDataPoints({"groupName", "groupDescription"})
@EventDescription(message="updated description for group '%s'", params="groupName")
public class GroupDescriptionUpdatedEvent extends GroupEvent {

  public GroupDescriptionUpdatedEvent(VOMSGroup g) {

    super(g);
  }

}
