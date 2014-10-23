/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.persistence.model.personal_info;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "personal_info")
public class PersonalInformationRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "VOMS_PI_SEQ")
  @SequenceGenerator(name = "VOMS_PI_SEQ", sequenceName = "VOMS_PI_SEQ")
  Long id;

  @ManyToOne
  @JoinColumn(name = "personal_info_type_id", nullable = false)
  PersonalInformationType type;

  String value;

  Boolean visible;

  public PersonalInformationRecord() {

    // TODO Auto-generated constructor stub
  }

  /**
   * @return the id
   */
  public Long getId() {

    return id;
  }

  /**
   * @return the type
   */
  public PersonalInformationType getType() {

    return type;
  }

  /**
   * @return the value
   */
  public String getValue() {

    return value;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(Long id) {

    this.id = id;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(PersonalInformationType type) {

    this.type = type;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(String value) {

    this.value = value;
  }

}
