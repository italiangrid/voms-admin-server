package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPVersion;

@EventDescription(
  message = "set version '%s' for AUP '%s' with URL '%s' as the enforced AUP version",
  params = { "aupVersion", "aupName", "aupVersionURL" })
public class AUPVersionSetActiveEvent extends AUPVersionEvent {

  public AUPVersionSetActiveEvent(AUP payload, AUPVersion v) {

    super(payload, v);
  }

}
