package org.glite.security.voms.admin.event.user.attribute;

import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.VOMSUserAttribute;


public class UserAttributeDeletedEvent extends UserAttributeEvent {

  public UserAttributeDeletedEvent(VOMSUser user, VOMSUserAttribute attribute) {

    super(user, attribute);
  }

}
