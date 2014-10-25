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

package org.glite.security.voms.admin.util;

import org.slf4j.Logger;

public class ExceptionUtils {

  public static void logError(Logger log, Throwable t) {

    log.error(t.getMessage());
    if (log.isDebugEnabled())
      log.error(t.getMessage(), t);

  }

  public static void logWarning(Logger log, Throwable t) {

    log.warn(t.getMessage());
    if (log.isDebugEnabled())
      log.warn(t.getMessage(), t);

  }

  public static void logInfo(Logger log, Throwable t) {

    log.info(t.getMessage());
    if (log.isDebugEnabled())
      log.info(t.getMessage(), t);
  }

}
