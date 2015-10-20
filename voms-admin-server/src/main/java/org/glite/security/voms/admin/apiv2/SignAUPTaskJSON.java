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
package org.glite.security.voms.admin.apiv2;

import java.util.Date;

import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;

public class SignAUPTaskJSON {

  Date creationDate;
  Date expirationDate;
  Date lastNotificationTime;

  public SignAUPTaskJSON() {

  }

  public Date getCreationDate() {

    return creationDate;
  }

  public void setCreationDate(Date creationDate) {

    this.creationDate = creationDate;
  }

  public Date getExpirationDate() {

    return expirationDate;
  }

  public void setExpirationDate(Date expirationDate) {

    this.expirationDate = expirationDate;
  }

  public Date getLastNotificationTime() {

    return lastNotificationTime;
  }

  public void setLastNotificationTime(Date lastNotificationTime) {

    this.lastNotificationTime = lastNotificationTime;
  }

  public static SignAUPTaskJSON from(SignAUPTask t) {

    if (t == null) {
      return null;
    }
    
    SignAUPTaskJSON task = new SignAUPTaskJSON();
    task.setCreationDate(t.getCreationDate());
    task.setExpirationDate(t.getExpiryDate());
    task.setLastNotificationTime(t.getLastNotificationTime());
    return task;
  }
}
