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
package org.glite.security.voms.admin.persistence.model.audit;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;
import org.hibernate.annotations.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Embeddable
@Table(name = "audit_event_data")
@org.hibernate.annotations.Table(appliesTo = "audit_event_data",
    indexes = {@Index(columnNames = {"name"}, name = "aed_name_idx"),
        @Index(columnNames = {"value"}, name = "aed_value_idx")})
public class AuditEventData {

  public static final Logger LOG = LoggerFactory.getLogger(AuditEventData.class);

  @Column(nullable = false)
  String name;

  @Column(nullable = false, length = 512)
  String value;

  public AuditEventData() {

  }

  public AuditEventData(String name, String value) {

    Validate.notEmpty(name);
    Validate.notEmpty(value);

    this.name = name;
    this.value = value;

    checkValueLength();

  }

  private void checkValueLength() {

    if (value.length() > 512) {
      // This should never happen
      LOG.error("Truncating AuditEventData value length, as it exceeds the limit. Value: {}",
          value);
      value = value.substring(0, 511);
    }

  }

  public String getName() {

    return name;
  }

  public void setName(String name) {

    this.name = name;
  }

  public String getValue() {

    return value;
  }

  public void setValue(String value) {

    this.value = value;
    checkValueLength();
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
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
    AuditEventData other = (AuditEventData) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

  @Override
  public String toString() {

    return "AuditEventData [name=" + name + ", value=" + value + "]";
  }

  public static AuditEventData newInstance(String name, String value) {

    return new AuditEventData(name, value);
  }
}
