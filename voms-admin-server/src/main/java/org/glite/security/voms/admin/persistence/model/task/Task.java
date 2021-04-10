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
package org.glite.security.voms.admin.persistence.model.task;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "task")
public abstract class Task {

  public enum TaskStatus {
    CREATED, REASSIGNED, COMPLETED, EXPIRED
  }

  @Id
  @Column(name = "task_id")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "task_type_id", nullable = false, updatable = false)
  TaskType type;

  Date creationDate;
  Date expiryDate;
  Date completionDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  TaskStatus status;

  @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "task", orphanRemoval = true)
  @Sort(type = SortType.NATURAL)
  @org.hibernate.annotations.Cascade(
    value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
  SortedSet<LogRecord> logRecords = new TreeSet<LogRecord>();

  @ManyToOne
  @JoinColumn(name = "usr_id")
  VOMSUser user;

  @ManyToOne
  @JoinColumn(name = "admin_id")
  VOMSAdmin admin;

  /**
   * @return the id
   */
  public Long getId() {

    return id;
  }

  /**
   * @return the typeInfo
   */
  public TaskType getType() {

    return type;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(Long id) {

    this.id = id;
  }

  /**
   * @param typeInfo
   *          the typeInfo to set
   */
  public void setType(TaskType typeInfo) {

    this.type = typeInfo;
  }

  /**
   * @return the creationDate
   */
  public Date getCreationDate() {

    return creationDate;
  }

  /**
   * @param creationDate
   *          the creationDate to set
   */
  public void setCreationDate(Date creationDate) {

    this.creationDate = creationDate;
  }

  /**
   * @return the status
   */
  public TaskStatus getStatus() {

    return status;
  }

  /**
   * @param status
   *          the status to set
   */
  public void setStatus(TaskStatus status) {

    this.status = status;
  }

  /**
   * @return the expiryDate
   */
  public Date getExpiryDate() {

    return expiryDate;
  }

  /**
   * @param expiryDate
   *          the expiryDate to set
   */
  public void setExpiryDate(Date expiryDate) {

    this.expiryDate = expiryDate;
  }

  /**
   * @return the completionDate
   */
  public Date getCompletionDate() {

    return completionDate;
  }

  /**
   * @param completionDate
   *          the completionDate to set
   */
  public void setCompletionDate(Date completionDate) {

    this.completionDate = completionDate;
  }

  public void setCompleted() {

    setCompletionDate(new Date());
    setStatus(TaskStatus.COMPLETED);
  }

  public void setExpired() {
    setStatus(TaskStatus.EXPIRED);
  }

  public boolean isExpired(){
    return status == TaskStatus.EXPIRED;
  }
  
  public boolean equals(Object other) {

    if (this == other)
      return true;

    if (!(other instanceof Task))
      return false;

    if (other == null)
      return false;

    Task that = (Task) other;

    EqualsBuilder builder = new EqualsBuilder();

    builder.append(type, that.type).append(creationDate, that.creationDate)
      .append(expiryDate, that.expiryDate)
      .append(completionDate, that.completionDate).append(status, that.status)
      .append(admin, that.admin).append(user, that.user);

    return builder.isEquals();

  }

  @Override
  public int hashCode() {

    HashCodeBuilder builder = new HashCodeBuilder(11, 67);

    builder.append(getType()).append(creationDate).append(expiryDate)
      .append(completionDate).append(status).append(admin).append(user);

    return builder.toHashCode();

  }

  /**
   * @return the logRecords
   */
  public SortedSet<LogRecord> getLogRecords() {

    return logRecords;
  }

  /**
   * @param logRecords
   *          the logRecords to set
   */
  public void setLogRecords(SortedSet<LogRecord> logRecords) {

    this.logRecords = logRecords;
  }

  /**
   * @return the user
   */
  public VOMSUser getUser() {

    return user;
  }

  /**
   * @return the admin
   */
  public VOMSAdmin getAdmin() {

    return admin;
  }

  /**
   * @param user
   *          the user to set
   */
  public void setUser(VOMSUser user) {

    this.user = user;
  }

  /**
   * @param admin
   *          the admin to set
   */
  public void setAdmin(VOMSAdmin admin) {

    this.admin = admin;
  }

  public long getDaysBeforeExpiration() {

    long now = new Date().getTime();
    return TimeUnit.MILLISECONDS.toDays(getExpiryDate().getTime() - now);

  }

  public boolean expiryDateInThePast() {
    long now = System.currentTimeMillis();
    
    return now > getExpiryDate().getTime();
  }
}
