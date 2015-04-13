package org.glite.security.voms.admin.event.vo.acl;

import org.glite.security.voms.admin.persistence.model.ACL;


public class ACLUpdatedEvent extends ACLEvent {

  public ACLUpdatedEvent(ACL acl) {

    super(acl);

  }

}
