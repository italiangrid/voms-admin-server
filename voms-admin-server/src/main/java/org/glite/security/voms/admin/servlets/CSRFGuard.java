/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */

/**

 Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2011.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 **/
package org.glite.security.voms.admin.servlets;

import javax.servlet.http.HttpServletRequest;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.configuration.VOMSConfigurationConstants;
import org.glite.security.voms.admin.error.VOMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSRFGuard {

  public static final Logger log = LoggerFactory.getLogger(CSRFGuard.class);

  public static final String CSRF_GUARD_HEADER_NAME = "X-VOMS-CSRF-GUARD";

  public static void checkCSRFGuard(HttpServletRequest request) {

    boolean csrfGuardLogOnly = VOMSConfiguration.instance().getBoolean(
      VOMSConfigurationConstants.VOMS_CSRF_GUARD_LOG_ONLY, false);

    if (request.getHeader(CSRF_GUARD_HEADER_NAME) == null) {

      log.warn(
        "Incoming request from {}:{} is missing CSRF prevention HTTP header",
        request.getRemoteAddr(), request.getRemotePort());

      if (!csrfGuardLogOnly)
        throw new VOMSException("CSRF header guard missing from request!");

    }
  }
}
