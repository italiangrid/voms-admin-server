package org.glite.security.voms.admin.event.vo.attribute;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSAttributeDescription;

@EventDescription(message="created attribute class '%s'", params={"attributeClassName"})
public class AttributeDescriptionCreatedEvent extends AttributeDescriptionEvent {

  public AttributeDescriptionCreatedEvent(VOMSAttributeDescription ad) {

    super(ad);

  }

}
