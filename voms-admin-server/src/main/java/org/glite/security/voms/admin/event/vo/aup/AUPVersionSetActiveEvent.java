package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPVersion;


public class AUPVersionSetActiveEvent extends AUPVersionEvent {

  public AUPVersionSetActiveEvent(AUP payload, AUPVersion v) {

    super(payload, v);
  }

}
