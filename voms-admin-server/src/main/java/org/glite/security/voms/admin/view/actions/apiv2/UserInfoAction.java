package org.glite.security.voms.admin.view.actions.apiv2;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.apiv2.JSONSerializer;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

@ParentPackage("json")
@Results({ @Result(name = BaseAction.SUCCESS, type = "json") })
public class UserInfoAction extends RestUserAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  VOMSUserJSON userInfo;

  @Override
  public void prepare() throws Exception {

    if (getModel() == null) {

      if ((certificateSubject == null) || (caSubject == null)) {
        return;
      }

      user = (VOMSUser) FindUserOperation.instance(certificateSubject,
        caSubject).execute();

      if (user == null) {
        addFieldError("certificateSubject",
          "No user found matching the provided certificate subject");
        addFieldError("caSubject",
          "No user found matching the provided certificate issuer subject");
        addActionError("User not found");
      }
    }
  }

  @Override
  public String execute() throws Exception {

    userInfo = JSONSerializer.serialize(getModel());

    return SUCCESS;
  }

  public VOMSUserJSON getUserInfo() {

    return userInfo;
  }

}
