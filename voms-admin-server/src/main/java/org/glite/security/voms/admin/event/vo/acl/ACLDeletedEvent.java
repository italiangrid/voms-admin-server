package org.glite.security.voms.admin.event.vo.acl;

import org.glite.security.voms.admin.persistence.model.ACL;

public class ACLDeletedEvent extends ACLEvent {

  public ACLDeletedEvent(ACL acl) {

    super(acl);

  }

}
