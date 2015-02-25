package org.glite.security.voms.admin.view.actions.dev;

import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.test.CreateRandomConfirmedMembershipRequests;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Result(name = BaseAction.SUCCESS, location = "/home/login.action",
  type = "redirect")
public class CreateRandomRequestsAction extends BaseDevAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public String execute() throws Exception {

    if (devEnabled()) {
      new CreateRandomConfirmedMembershipRequests().run();
    }
    
    return SUCCESS;
  }

}
