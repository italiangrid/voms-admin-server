package org.glite.security.voms.admin.event.vo.group;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;


public class GroupEditedEvent extends GroupEvent {

  public GroupEditedEvent(VOMSGroup g) {

    super(g);
  }

}
