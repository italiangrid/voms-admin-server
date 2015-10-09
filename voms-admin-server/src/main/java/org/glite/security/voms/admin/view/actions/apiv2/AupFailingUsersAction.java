package org.glite.security.voms.admin.view.actions.apiv2;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.apiv2.JSONSerializer;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.operations.users.ListAUPFailingUsersOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("json")
@Results({ @Result(name = BaseAction.SUCCESS, type = "json") })
public class AupFailingUsersAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  List<VOMSUserJSON> aupFailingUsers;

  @Override
  public String execute() throws Exception {

    aupFailingUsers = JSONSerializer.serialize(
      new ListAUPFailingUsersOperation().execute());
    
    return BaseAction.SUCCESS;
  }

  public List<VOMSUserJSON> getAupFailingUsers() {

    return aupFailingUsers;
  }

}
