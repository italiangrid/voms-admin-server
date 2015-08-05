package org.glite.security.voms.admin.event.vo.acl;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.ACL;

@EventDescription(message = "created ACL for context '%s'",
  params = { "aclContext" })
public class ACLCreatedEvent extends ACLEvent {

  public ACLCreatedEvent(ACL acl) {

    super(acl);
    
  }

}
