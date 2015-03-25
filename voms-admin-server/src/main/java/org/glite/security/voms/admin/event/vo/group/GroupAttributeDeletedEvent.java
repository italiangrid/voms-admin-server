package org.glite.security.voms.admin.event.vo.group;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSGroupAttribute;

public class GroupAttributeDeletedEvent extends GroupAttributeEvent {

  public GroupAttributeDeletedEvent(VOMSGroup g,
    VOMSGroupAttribute groupAttribute) {

    super(g, groupAttribute);

  }

}
