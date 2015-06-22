package org.glite.security.voms.admin.event.vo.attribute;

import org.glite.security.voms.admin.persistence.model.VOMSAttributeDescription;

public class AttributeDescriptionCreatedEvent extends AttributeDescriptionEvent {

  public AttributeDescriptionCreatedEvent(VOMSAttributeDescription ad) {

    super(ad);

  }

}
