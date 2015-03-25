package org.glite.security.voms.admin.event.user.attribute;

import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUserAttribute;


public class UserAttributeSetEvent extends UserAttributeEvent {

  public UserAttributeSetEvent(VOMSUser payload,
    VOMSUserAttribute attribute) {

    super(payload, attribute);

  }

}
