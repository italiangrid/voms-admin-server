package org.glite.security.voms.admin.event.user.attribute;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUserAttribute;

@EventDescription(
  message = "set attribute '%s' for user '%s %s' to value: '%s'", params = {
    "attributeName", "userName", "userSurname", "attributeValue" })
public class UserAttributeSetEvent extends UserAttributeEvent {

  public UserAttributeSetEvent(VOMSUser payload, VOMSUserAttribute attribute) {

    super(payload, attribute);

  }

}
