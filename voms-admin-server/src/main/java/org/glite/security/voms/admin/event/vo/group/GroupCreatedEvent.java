package org.glite.security.voms.admin.event.vo.group;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;

public class GroupCreatedEvent extends GroupEvent {

  public GroupCreatedEvent(VOMSGroup g) {

    super(g);
  }

}
