package org.glite.security.voms.admin.event.vo.group;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;

public class GroupDescriptionUpdatedEvent extends GroupEvent {

  public GroupDescriptionUpdatedEvent(VOMSGroup g) {

    super(g);
  }

}
