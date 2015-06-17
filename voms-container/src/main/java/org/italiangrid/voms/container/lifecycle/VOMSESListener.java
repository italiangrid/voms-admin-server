package org.italiangrid.voms.container.lifecycle;

import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VOMSESListener implements LifeCycle.Listener {

  public static final Logger log = LoggerFactory.getLogger("Vomses");

  @Override
  public void lifeCycleStarting(LifeCycle event) {

  }

  @Override
  public void lifeCycleStarted(LifeCycle event) {

    log.info("VOMSES status handler started.");
  }

  @Override
  public void lifeCycleFailure(LifeCycle event, Throwable cause) {

    log.error("VOMSES status handler error: {}", cause.getMessage(), cause);
  }

  @Override
  public void lifeCycleStopping(LifeCycle event) {

  }

  @Override
  public void lifeCycleStopped(LifeCycle event) {

    log.info("VOMSES status handler stopped.");
  }

}
