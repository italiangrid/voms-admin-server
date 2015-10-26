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
package org.glite.security.voms.admin.view.actions.audit;

import java.util.Date;

public class AuditLogSearchParams {

  Date fromTime;
  Date toTime;

  String filterType;
  String filterString;

  Integer firstResult;
  Integer maxResults;

  public AuditLogSearchParams() {

  }

  public Date getFromTime() {

    return fromTime;
  }

  public void setFromTime(Date fromTime) {

    this.fromTime = fromTime;
  }

  public Date getToTime() {

    return toTime;
  }

  public void setToTime(Date toTime) {

    this.toTime = toTime;
  }

  public String getFilterType() {

    return filterType;
  }

  public void setFilterType(String filterType) {

    this.filterType = filterType;
  }

  public String getFilterString() {

    return filterString;
  }

  public void setFilterString(String filterString) {

    this.filterString = filterString;
  }

  public Integer getFirstResult() {

    return firstResult;
  }

  public void setFirstResult(Integer firstResult) {

    this.firstResult = firstResult;
  }

  public Integer getMaxResults() {

    return maxResults;
  }

  public void setMaxResults(Integer maxResults) {

    this.maxResults = maxResults;
  }

  @Override
  public String toString() {

    return "AuditLogSearchParams [fromTime=" + fromTime + ", toTime=" + toTime
      + ", filterType=" + filterType + ", filterString=" + filterString + "]";
  }
  
  

}
