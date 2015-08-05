package org.glite.security.voms.admin.event.vo.attribute;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSAttributeDescription;

@EventDescription(message = "deleted attribute class '%s'",
  params = { "attributeClassName" })
public class AttributeDescriptionDeletedEvent extends AttributeDescriptionEvent {

  public AttributeDescriptionDeletedEvent(VOMSAttributeDescription ad) {

    super(ad);

  }

}
