package org.glite.security.voms.admin.view.actions.user;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;


@Results({
  @Result(name = UserActionSupport.SUCCESS, location = "userChangeOrgDbId")
})
public class ChangeOrgdbIdAction extends UserActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
}
