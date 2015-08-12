package org.glite.security.voms.admin.view.actions.group;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({ @Result(name = BaseAction.SUCCESS, location = "groupEditDescription") })
public class EditGroupDescriptionAction extends GroupActionSupport {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
}
