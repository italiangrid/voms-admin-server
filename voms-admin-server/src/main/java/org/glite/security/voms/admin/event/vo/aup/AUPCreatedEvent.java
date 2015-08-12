package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.persistence.model.AUP;

public class AUPCreatedEvent extends AUPEvent {

  public AUPCreatedEvent(AUP payload) {

    super(payload);
  }

}
