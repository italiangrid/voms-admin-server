package org.glite.security.voms.admin.event.vo.acl;

import org.glite.security.voms.admin.event.EventDescription;
import org.glite.security.voms.admin.persistence.model.ACL;

@EventDescription(message = "removed ACL for context '%s'",
  params = { "aclContext" })
public class ACLDeletedEvent extends ACLEvent {

  public ACLDeletedEvent(ACL acl) {

    super(acl);

  }

}
