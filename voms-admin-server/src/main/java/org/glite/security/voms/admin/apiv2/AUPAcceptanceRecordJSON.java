package org.glite.security.voms.admin.apiv2;

import java.util.Date;

import org.glite.security.voms.admin.persistence.model.AUPAcceptanceRecord;

public class AUPAcceptanceRecordJSON {

  String aupVersion;
  Date lastAcceptanceDate;
  long daysBeforeExpiration;

  public AUPAcceptanceRecordJSON() {

  }

  public String getAupVersion() {

    return aupVersion;
  }

  public void setAupVersion(String aupVersion) {

    this.aupVersion = aupVersion;
  }

  public Date getLastAcceptanceDate() {

    return lastAcceptanceDate;
  }

  public void setLastAcceptanceDate(Date lastAcceptanceDate) {

    this.lastAcceptanceDate = lastAcceptanceDate;
  }

  public long getDaysBeforeExpiration() {

    return daysBeforeExpiration;
  }

  public void setDaysBeforeExpiration(long daysBeforeExpiration) {

    this.daysBeforeExpiration = daysBeforeExpiration;
  }

  public static AUPAcceptanceRecordJSON from(AUPAcceptanceRecord rec) {

    AUPAcceptanceRecordJSON r = new AUPAcceptanceRecordJSON();

    r.setAupVersion(rec.getAupVersion().getVersion());
    if (rec.getDaysBeforeExpiration() < 0){
      r.setDaysBeforeExpiration(0);
    }else{
      r.setDaysBeforeExpiration(rec.getDaysBeforeExpiration());
    }
    r.setLastAcceptanceDate(rec.getLastAcceptanceDate());

    return r;
  }

}
