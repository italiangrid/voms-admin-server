package org.glite.security.voms.admin.event.user.aup;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

@EventDescription(message = "requested a new AUP signature from user '%s %s'",
  params = { "userName", "userSurname" })
public class UserAUPSignatureRequestedEvent extends UserAUPEvent {

  public UserAUPSignatureRequestedEvent(VOMSUser user, AUP aup) {

    super(user, aup);
  }

}
