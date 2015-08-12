package org.glite.security.voms.admin.event.vo.group;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;

public class GroupDeletedEvent extends GroupEvent {

  public GroupDeletedEvent(VOMSGroup g) {

    super(g);
  }

}
