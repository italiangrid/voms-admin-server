package org.glite.security.voms.admin.event.vo.admin;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;

@EventDescription(message="deleted admin with subject '%s'", params={"dn"})
public class AdminDeletedEvent extends AdminEvent {

  public AdminDeletedEvent(VOMSAdmin admin) {

    super(admin);

  }

}
