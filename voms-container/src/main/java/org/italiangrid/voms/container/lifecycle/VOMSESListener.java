/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
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
