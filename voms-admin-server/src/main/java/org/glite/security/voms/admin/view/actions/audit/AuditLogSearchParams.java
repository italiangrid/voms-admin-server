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
