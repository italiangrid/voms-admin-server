package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.AUP;

@EventDescription(
  message = "changed reacceptance period for AUP '%s' to %s days", params = {
    "aupName", "aupReacceptancePeriod" })
public class AUPChangedPeriodEvent extends AUPEvent {

  public AUPChangedPeriodEvent(AUP payload) {

    super(payload);
  }

}
