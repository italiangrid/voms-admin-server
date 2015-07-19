package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPVersion;

@EventDescription(message = "updated version '%s' for AUP '%s' with URL '%s'",
  params = { "aupVersion", "aupName", "aupVersionURL" })
public class AUPVersionUpdatedEvent extends AUPVersionEvent {

  public AUPVersionUpdatedEvent(AUP payload, AUPVersion v) {

    super(payload, v);
  }

}
