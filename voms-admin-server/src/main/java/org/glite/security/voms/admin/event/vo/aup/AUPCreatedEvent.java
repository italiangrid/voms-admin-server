package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.AUP;

@EventDescription(message = "created AUP '%s'", params = { "aupName" })
public class AUPCreatedEvent extends AUPEvent {

  public AUPCreatedEvent(AUP payload) {

    super(payload);
  }

}
