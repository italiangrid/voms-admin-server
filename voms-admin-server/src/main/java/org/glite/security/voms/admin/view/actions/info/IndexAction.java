package org.glite.security.voms.admin.view.actions.info;

import java.util.Locale;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@Results({ @Result(name = BaseAction.SUCCESS, location = "info") })
public class IndexAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public CurrentAdmin getCurrentAdmin() {

    return CurrentAdmin.instance();
  }
  
  @Override
  public String execute() throws Exception {
  
    ActionContext.getContext().setLocale(Locale.US);
    return super.execute();
  }

}
