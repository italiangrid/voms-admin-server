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
package org.glite.security.voms.admin.apiv2;

import java.util.Date;

import org.glite.security.voms.admin.persistence.model.AUPAcceptanceRecord;

public class AUPAcceptanceRecordJSON {

  String aupVersion;
  Date lastAcceptanceDate;
  long daysBeforeExpiration;
  boolean valid;

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

  
  public boolean isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  public static AUPAcceptanceRecordJSON from(AUPAcceptanceRecord rec) {

    AUPAcceptanceRecordJSON r = new AUPAcceptanceRecordJSON();

    r.setAupVersion(rec.getAupVersion().getVersion());
    r.setValid(rec.getValid());
    
    if (rec.getDaysBeforeExpiration() < 0){
      r.setDaysBeforeExpiration(0);
    }else{
      r.setDaysBeforeExpiration(rec.getDaysBeforeExpiration());
    }
    r.setLastAcceptanceDate(rec.getLastAcceptanceDate());

    return r;
  }

}
