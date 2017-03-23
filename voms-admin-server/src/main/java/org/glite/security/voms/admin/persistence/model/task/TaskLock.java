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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "task_lock")
public class TaskLock {

  @Id
  @Column(name = "task_name", columnDefinition = "varchar(64)")
  String taskName;

  @Column(name = "service_id", columnDefinition = "varchar(255)",
    nullable = false)
  String serviceId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at", nullable = false)
  Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "finished_at")
  Date finishedAt;

  public TaskLock() {
  }

  public String getTaskName() {

    return taskName;
  }

  public void setTaskName(String taskName) {

    this.taskName = taskName;
  }

  public String getServiceId() {

    return serviceId;
  }

  public void setServiceId(String serviceId) {

    this.serviceId = serviceId;
  }

  public Date getCreatedAt() {

    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {

    this.createdAt = createdAt;
  }

  public Date getFinishedAt() {

    return finishedAt;
  }

  public void setFinishedAt(Date finishedAt) {

    this.finishedAt = finishedAt;
  }

  public boolean isTaskDone(){
    return finishedAt != null;
  }
  
  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TaskLock other = (TaskLock) obj;
    if (taskName == null) {
      if (other.taskName != null)
        return false;
    } else if (!taskName.equals(other.taskName))
      return false;
    return true;
  }

  @Override
  public String toString() {

    return "TaskLock [taskName=" + taskName + ", serviceId=" + serviceId
      + ", createdAt=" + createdAt + ", finishedAt=" + finishedAt + "]";
  }

}
