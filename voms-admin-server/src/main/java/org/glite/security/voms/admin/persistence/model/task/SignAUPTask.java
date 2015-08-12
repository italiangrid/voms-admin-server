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
package org.glite.security.voms.admin.persistence.model.task;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.glite.security.voms.admin.persistence.model.AUP;

@Entity
@Table(name = "sign_aup_task")
public class SignAUPTask extends Task implements Serializable {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  @ManyToOne
  @JoinColumn(name = "aup_id", nullable = false)
  AUP aup;

  @Column(name = "last_notification_time")
  Date lastNotificationTime;

  public SignAUPTask() {

  }

  public SignAUPTask(TaskType tt, AUP a, Date expiryDate) {

    type = tt;
    aup = a;
    this.expiryDate = expiryDate;
    creationDate = new Date();
    status = TaskStatus.CREATED;

    addLogRecord(getCreationDate());

  }

  /**
   * @return the aup
   */
  public AUP getAup() {

    return aup;
  }

  /**
   * @param aup
   *          the aup to set
   */
  public void setAup(AUP aup) {

    this.aup = aup;
  }

  @Override
  public String toString() {

    return String
      .format(
        "SignAUPTask[id:%d,status:%s,expires:%s,lastNotificationTime:%s,user:%s]",
        getId(), getStatus(), getExpiryDate(),
        lastNotificationTime,
        getUser().getId());

  }

  @Override
  public boolean equals(Object other) {

    if (this == other){
      return true;
    }
    
    if (!(other instanceof Task)){
      return false;
    }
    
    if (other == null){
      return false;
    }
    
    SignAUPTask that = (SignAUPTask) other;

    EqualsBuilder builder = new EqualsBuilder();
    return builder.appendSuper(super.equals(other)).append(aup, that.aup)
      .isEquals();

  }

  @Override
  public int hashCode() {

    HashCodeBuilder builder = new HashCodeBuilder(17, 57).appendSuper(
      super.hashCode()).append(aup);
    return builder.toHashCode();
  }

  public Date getLastNotificationTime() {

    return lastNotificationTime;
  }

  public void setLastNotificationTime(Date lastNotificationTime) {

    this.lastNotificationTime = lastNotificationTime;
  }

}
