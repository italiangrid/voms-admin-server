package org.glite.security.voms.admin.view.actions.test;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.view.actions.BaseAction;

@Results({ @Result(name = BaseAction.SUCCESS, location = "test") })
public class TestAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

}
