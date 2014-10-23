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
package org.glite.security.voms.admin.core.tasks;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUncaughtExceptionHandler implements UncaughtExceptionHandler {

  public static final Logger log = LoggerFactory
    .getLogger(ThreadUncaughtExceptionHandler.class);

  public void uncaughtException(Thread t, Throwable e) {

    String errorMessage = String.format(
      "Thread (%d - '%s') uncaught exception: %s at line %d pf %s%n",
      t.getId(), t.getName(), e.toString(),
      e.getStackTrace()[0].getLineNumber(), e.getStackTrace()[0].getFileName());

    log.error(errorMessage, e);

  }

}
