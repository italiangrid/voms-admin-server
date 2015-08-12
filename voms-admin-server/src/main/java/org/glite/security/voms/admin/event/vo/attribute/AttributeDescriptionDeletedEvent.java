package org.glite.security.voms.admin.event.vo.attribute;

import org.glite.security.voms.admin.persistence.model.VOMSAttributeDescription;

public class AttributeDescriptionDeletedEvent extends AttributeDescriptionEvent {

  public AttributeDescriptionDeletedEvent(VOMSAttributeDescription ad) {

    super(ad);

  }

}
