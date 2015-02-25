package org.glite.security.voms.admin.view.actions.dev;

import org.glite.security.voms.admin.view.actions.BaseAction;


public class BaseDevAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private static final String VOMS_DEV_PROPERTY = "voms.dev";

  public boolean devEnabled(){
    return System.getProperty(VOMS_DEV_PROPERTY) != null;
  }

}
