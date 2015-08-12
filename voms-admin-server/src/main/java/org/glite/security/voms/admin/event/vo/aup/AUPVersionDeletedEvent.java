package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPVersion;

public class AUPVersionDeletedEvent extends AUPVersionEvent {

  public AUPVersionDeletedEvent(AUP payload, AUPVersion v) {

    super(payload, v);

  }

}
