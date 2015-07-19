package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.AUP;

@EventDescription(
  message = "triggered the reacceptance for version '%s' of AUP '%s'",
  params = { "aupVersion", "aupName" })
public class TriggeredForcedReacceptanceEvent extends AUPEvent {

  public TriggeredForcedReacceptanceEvent(AUP payload) {

    super(payload);
  }

}
