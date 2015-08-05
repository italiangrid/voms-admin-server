package org.italiangrid.voms.container.lifecycle;

import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.component.LifeCycle.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerListener implements Listener {

  public static final Logger log = LoggerFactory.getLogger("Server");

  @Override
  public void lifeCycleStarting(LifeCycle event) {

  }

  @Override
  public void lifeCycleStarted(LifeCycle event) {

    log.info("VOMS Admin server started.");
  }

  @Override
  public void lifeCycleFailure(LifeCycle event, Throwable cause) {

    log.error("VOMS Admin server error: {}", cause.getMessage(), cause);
  }

  @Override
  public void lifeCycleStopping(LifeCycle event) {

    log.info("VOMS Admin server stopping.");
  }

  @Override
  public void lifeCycleStopped(LifeCycle event) {

    log.info("VOMS Admin server stopped.");
  }

}
