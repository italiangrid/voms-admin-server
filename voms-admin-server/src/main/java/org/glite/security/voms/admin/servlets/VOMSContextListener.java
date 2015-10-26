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
package org.glite.security.voms.admin.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glite.security.voms.admin.core.VOMSService;
import org.glite.security.voms.admin.error.VOMSException;

public class VOMSContextListener implements ServletContextListener {

  static final Logger log = LoggerFactory.getLogger(VOMSContextListener.class);

  public VOMSContextListener() {

    super();

  }

  public void contextInitialized(ServletContextEvent ctxtEvent) {

    try {

      VOMSService.start(ctxtEvent.getServletContext());

    } catch (VOMSException e) {

      log.error("VOMS-Admin setup failure!", e);

    }

  }

  public void contextDestroyed(ServletContextEvent ctxtEvent) {

    VOMSService.stop();

  }

}
