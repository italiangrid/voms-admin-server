package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.persistence.model.AUP;

public class AUPChangedPeriodEvent extends AUPEvent {

  public AUPChangedPeriodEvent(AUP payload) {

    super(payload);
  }

}
