package org.glite.security.voms.admin.event.user;

import org.glite.security.voms.admin.event.EventCategory;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class UserLifecycleEvent extends UserEvent {

  protected UserLifecycleEvent(VOMSUser payload) {

    super(EventCategory.UserLifecycleEvent, payload);

  }
}
