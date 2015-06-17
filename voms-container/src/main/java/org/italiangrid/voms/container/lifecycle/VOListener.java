package org.italiangrid.voms.container.lifecycle;

import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum VOListener implements LifeCycle.Listener {

  INSTANCE;

  public static final Logger log = LoggerFactory.getLogger("VO");

  @Override
  public void lifeCycleStarting(LifeCycle event) {

    WebAppContext ctxt = (WebAppContext) event;
    log.info("Starting VO {}...", ctxt.getInitParameter("VO_NAME"));

  }

  @Override
  public void lifeCycleStarted(LifeCycle event) {

    WebAppContext ctxt = (WebAppContext) event;
    log.info("VO {} started.", ctxt.getInitParameter("VO_NAME"));
  }

  @Override
  public void lifeCycleFailure(LifeCycle event, Throwable cause) {

    WebAppContext ctxt = (WebAppContext) event;
    log.error("VO {} encountered a fatal error: " + cause.getMessage(),
      ctxt.getInitParameter("VO_NAME"), cause);

  }

  @Override
  public void lifeCycleStopping(LifeCycle event) {

    WebAppContext ctxt = (WebAppContext) event;
    log.info("Stopping VO {}...", ctxt.getInitParameter("VO_NAME"));
  }

  @Override
  public void lifeCycleStopped(LifeCycle event) {

    WebAppContext ctxt = (WebAppContext) event;
    log.info("VO {} stopped.", ctxt.getInitParameter("VO_NAME"));
  }

}
