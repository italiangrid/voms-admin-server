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
package org.glite.security.voms.admin.core.tasks;

import org.italiangrid.voms.aa.x509.stats.ACEndpointStats;
import org.italiangrid.voms.aa.x509.stats.ExecutionTimeStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintX509AAStatsTask implements Runnable {

  public static final Logger log = LoggerFactory
    .getLogger(PrintX509AAStatsTask.class);

  public static final long DEFAULT_PERIOD_IN_SECS = 60;

  public PrintX509AAStatsTask() {

  }

  @Override
  public void run() {

    ExecutionTimeStats stats = ACEndpointStats.INSTANCE.getStats();

    log.info("AC endpoint execution time (last {} seconds) (in msecs): N={}, "
      + "max={}, min={}, avg={}.",
      new Object[] { DEFAULT_PERIOD_IN_SECS, stats.getCount(), stats.getMax(),
        stats.getMin(), stats.getMean() });
  }
}
