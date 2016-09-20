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
package org.glite.security.voms.admin.persistence.model.personal_info;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "personal_info_type")
public class PersonalInformationType {

  public enum Type {
    STRING, PHONE_NUMBER, EMAIL_ADDRESS, URL
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO,
    generator = "VOMS_PI_TYPE_SEQ")
  @SequenceGenerator(name = "VOMS_PI_TYPE_SEQ",
    sequenceName = "VOMS_PI_TYPE_SEQ")
  Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true)
  Type type;

  String description;

  /**
   * @return the id
   */
  public Long getId() {

    return id;
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
   * @param description
   *          the description to set
   */
  public void setDescription(String description) {

    this.description = description;
  }

  /**
   * @return the type
   */
  public Type getType() {

    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(Type type) {

    this.type = type;
  }

}
