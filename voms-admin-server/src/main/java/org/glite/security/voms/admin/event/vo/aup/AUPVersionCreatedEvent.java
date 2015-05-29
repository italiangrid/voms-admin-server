package org.glite.security.voms.admin.event.vo.aup;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPVersion;


@EventDescription(message = "created version '%s' for AUP '%s' with URL '%s'", params = { "aupVersion", "aupName", "aupVersionURL" })
public class AUPVersionCreatedEvent extends AUPVersionEvent {

  public AUPVersionCreatedEvent(AUP payload, AUPVersion v) {

    super(payload, v);
   
  }

}
