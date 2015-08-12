/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
