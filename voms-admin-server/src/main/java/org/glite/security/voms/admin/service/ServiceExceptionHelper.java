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
package org.glite.security.voms.admin.service;

import org.slf4j.Logger;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.hibernate.JDBCException;

public class ServiceExceptionHelper {

  public static void handleServiceException(Logger log, RuntimeException t) {

    // If the exception is a DB exception, clean out the session gracefully
    if (t instanceof JDBCException) {

      Throwable cause = t.getCause();

      log.error("Database error caught: " + cause.getMessage());

      // Print stack trace in logs if log is debug enabled.
      if (log.isDebugEnabled())
        log.error("Database error caught: " + cause.getMessage(), t);

      try {

        HibernateFactory.rollbackTransaction();

      } finally {

        HibernateFactory.closeSession();

      }

    } else {

      if (log.isDebugEnabled())
        log.error(t.getClass().getSimpleName() + " exception caught: ", t);
      else
        log.error(t.getClass().getSimpleName() + " exception caught: "
          + t.getMessage());
    }

    throw t;
  }

}
