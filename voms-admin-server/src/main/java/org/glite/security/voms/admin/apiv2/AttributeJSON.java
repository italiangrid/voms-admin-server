package org.glite.security.voms.admin.apiv2;

import org.glite.security.voms.admin.persistence.model.VOMSUserAttribute;

public class AttributeJSON {

  String name;
  String value;

  public AttributeJSON() {

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

  public static AttributeJSON fromVOMSUserAttribute(VOMSUserAttribute attr) {

    if (attr == null) {
      return null;
    }

    AttributeJSON a = new AttributeJSON();
    a.setName(attr.getName());
    a.setValue(attr.getValue());

    return a;
  }

}
