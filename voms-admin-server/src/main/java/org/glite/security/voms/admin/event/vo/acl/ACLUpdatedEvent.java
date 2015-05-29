package org.glite.security.voms.admin.event.vo.acl;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.event.MainEventDataPoints;
import org.glite.security.voms.admin.persistence.model.ACL;

@EventDescription(message = "updated ACL for context '%s'",
  params = { "aclContext" })
public class ACLUpdatedEvent extends ACLEvent {

  public ACLUpdatedEvent(ACL acl) {

    super(acl);

  }

}
