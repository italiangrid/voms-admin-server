package org.glite.security.voms.admin.view.actions.role;

import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Result(name=BaseAction.SUCCESS, location="createRole.jsp")
public class NewRoleAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public String execute() throws Exception {
 
    return BaseAction.SUCCESS;
  }

}
