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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "task_type")
public class TaskType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO,
    generator = "VOMS_TASK_TYPE_SEQ")
  @SequenceGenerator(name = "VOMS_TASK_TYPE_SEQ",
    sequenceName = "VOMS_TASK_TYPE_SEQ")
  Long id;

  @Column(nullable = false, unique = true)
  String name;

  String description;

  /**
   * @return the id
   */

  public Long getId() {

    return id;
  }

  /**
   * @return the typeName
   */

  public String getName() {

    return name;
  }

  /**
   * @return the description
   */
  public String getDescription() {

    return description;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(Long id) {

    this.id = id;
  }

  /**
   * @param typeName
   *          the typeName to set
   */
  public void setName(String typeName) {

    this.name = typeName;
  }

  /**
   * @param description
   *          the description to set
   */
  public void setDescription(String description) {

    this.description = description;
  }

  public boolean equals(Object other) {

    if (this == other)
      return true;

    if (!(other instanceof TaskType))
      return false;

    if (other == null)
      return false;

    TaskType that = (TaskType) other;

    return this.getName().equals(that.getName());

  }

  @Override
  public int hashCode() {

    if (getName() == null)
      return 0;

    return getName().hashCode();

  }

  @Override
  public String toString() {

    return String.format("[id:%d, name:%s]", getId(), getName());

  }
}
