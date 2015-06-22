package org.glite.security.voms.admin.event.user.aup;

import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class UserAUPSignatureRequestedEvent extends UserAUPEvent {

  public UserAUPSignatureRequestedEvent(VOMSUser user, AUP aup) {

    super(user, aup);
  }

}
