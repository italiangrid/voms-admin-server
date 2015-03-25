package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.persistence.model.AUP;


public class TriggeredForcedReacceptanceEvent extends AUPEvent {

  public TriggeredForcedReacceptanceEvent(AUP payload) {

    super(payload);
  }

}
