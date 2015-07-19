package org.glite.security.voms.admin.event.vo.admin;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;

@EventDescription(message="created admin with subject '%s'", params={"dn"})
public class AdminCreatedEvent extends AdminEvent {

  public AdminCreatedEvent(VOMSAdmin admin) {

    super(admin);
  }

}
