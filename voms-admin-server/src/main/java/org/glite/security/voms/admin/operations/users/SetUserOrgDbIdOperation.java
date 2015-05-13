package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.event.EventManager;
import org.glite.security.voms.admin.event.user.UserOrgDbIdUpdatedEvent;
import org.glite.security.voms.admin.operations.BaseVoRWOperation;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class SetUserOrgDbIdOperation extends BaseVoRWOperation {

  private VOMSUser user;
  private Long orgDbId;

  public SetUserOrgDbIdOperation(VOMSUser user, Long orgDbId) {

    this.user = user;
    this.orgDbId = orgDbId;
  }

  @Override
  protected Object doExecute() {

    user.setOrgDbId(orgDbId);
    
    EventManager.instance().dispatch(new UserOrgDbIdUpdatedEvent(user));
    return user;
  }
  
}
