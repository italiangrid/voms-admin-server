package org.glite.security.voms.admin.event.vo.admin;

import org.glite.security.voms.admin.persistence.model.VOMSAdmin;

public class AdminCreatedEvent extends AdminEvent {

  public AdminCreatedEvent(VOMSAdmin admin) {

    super(admin);
  }

}
