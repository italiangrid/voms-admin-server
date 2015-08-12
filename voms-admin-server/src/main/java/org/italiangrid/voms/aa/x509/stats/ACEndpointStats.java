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
package org.italiangrid.voms.aa.x509.stats;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public enum ACEndpointStats {

  INSTANCE;

  private SummaryStatistics stats;

  private ACEndpointStats() {

    stats = new SummaryStatistics();
  }

  public synchronized void addValue(double value) {

    stats.addValue(value);
  }

  public synchronized ExecutionTimeStats getStats() {

    ExecutionTimeStats s = ExecutionTimeStats.fromSummaryStats(stats);
    stats.clear();
    return s;
  }

  public synchronized double getAverageExecutionTime() {

    return stats.getMean();
  }

  public synchronized double getMaximumExecutionTime() {

    return stats.getMax();
  }

  public synchronized double getMinimumExecutionTime() {

    return stats.getMin();
  }

  public synchronized void clear() {

    stats.clear();
  }
}