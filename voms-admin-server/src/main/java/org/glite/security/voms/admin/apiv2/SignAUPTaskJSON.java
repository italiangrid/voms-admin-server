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
