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
package org.italiangrid.voms.aa.x509.stats;

import org.apache.commons.math3.stat.descriptive.StatisticalSummary;

public class ExecutionTimeStats {

  double min;
  double max;
  double mean;
  long count;

  private ExecutionTimeStats() {

  }

  public static ExecutionTimeStats fromSummaryStats(StatisticalSummary stats) {

    ExecutionTimeStats v = new ExecutionTimeStats();

    v.setMax(stats.getMax());
    v.setMin(stats.getMin());
    v.setMean(stats.getMean());
    v.setCount(stats.getN());
    return v;
  }

  /**
   * @return the min
   */
  public double getMin() {

    return min;
  }

  /**
   * @param min
   *          the min to set
   */
  public void setMin(double min) {

    this.min = min;
  }

  /**
   * @return the max
   */
  public double getMax() {

    return max;
  }

  /**
   * @param max
   *          the max to set
   */
  public void setMax(double max) {

    this.max = max;
  }

  /**
   * @return the mean
   */
  public double getMean() {

    return mean;
  }

  /**
   * @param mean
   *          the mean to set
   */
  public void setMean(double mean) {

    this.mean = mean;
  }

  /**
   * @return the count
   */
  public long getCount() {

    return count;
  }

  /**
   * @param count
   *          the count to set
   */
  public void setCount(long count) {

    this.count = count;
  }

}
