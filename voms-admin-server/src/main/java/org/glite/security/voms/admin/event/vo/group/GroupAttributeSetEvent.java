package org.glite.security.voms.admin.event.vo.group;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSGroupAttribute;

public class GroupAttributeSetEvent extends GroupAttributeEvent {

  public GroupAttributeSetEvent(VOMSGroup g, VOMSGroupAttribute groupAttribute) {

    super(g, groupAttribute);

  }

}
