package org.glite.security.voms.admin.view.util;

import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.DispatcherListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DevModeEnabler implements DispatcherListener {

  public static final Logger LOG = LoggerFactory.getLogger(DevModeEnabler.class);
  
  public DevModeEnabler() {
  }

  @Override
  public void dispatcherInitialized(Dispatcher du) {
    LOG.warn("Enabling dev mode on Struts2 dispatcher. "
      + "This will have a serious impact on the service performace.");
    
    du.setDevMode("true");
  }

  @Override
  public void dispatcherDestroyed(Dispatcher du) {


  }

}
