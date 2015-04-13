package org.glite.security.voms.admin.event.vo.admin;

import org.glite.security.voms.admin.persistence.model.VOMSAdmin;

public class AdminDeletedEvent extends AdminEvent {

  public AdminDeletedEvent(VOMSAdmin admin) {

    super(admin);

  }

}
