package org.glite.security.voms.admin.persistence.model.audit;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.Validate;

@Embeddable
public class AuditEventData {

  @Column(nullable = false)
  String name;

  @Column(nullable = false)
  String value;

  public AuditEventData() {

  }

  public AuditEventData(String name, String value) {

    Validate.notEmpty(name);
    Validate.notEmpty(value);

    this.name = name;
    this.value = value;
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

  public static AuditEventData newInstance(String name, String value){
    return new AuditEventData(name, value);
  }
}
