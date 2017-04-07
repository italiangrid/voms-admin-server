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
package org.glite.security.voms.admin.persistence.model.attribute;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.service.attributes.AttributeValue;

@Entity
@Table(name = "group_attrs")
public class VOMSGroupAttribute implements Serializable, GenericAttributeValue {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  @Id
  @ManyToOne
  @JoinColumn(name = "a_id")
  VOMSAttributeDescription attributeDescription;

  @Id
  @ManyToOne
  @JoinColumn(name = "g_id")
  VOMSGroup group;

  @Column(name = "a_value")
  String value;

  @Transient
  String context;

  public VOMSGroupAttribute() {

  }

  public static VOMSGroupAttribute instance(VOMSAttributeDescription desc,
    String value, VOMSGroup g) {

    return new VOMSGroupAttribute(desc, value, g);
  }

  protected VOMSGroupAttribute(VOMSAttributeDescription desc, String value,
    VOMSGroup g) {

    this.attributeDescription = desc;
    this.value = value;
    this.group = g;

  }

  public String getContext() {

    return group.getName();
  }

  public VOMSGroup getGroup() {

    return group;
  }

  public void setGroup(VOMSGroup group) {

    this.group = group;
  }

  public boolean equals(Object other) {

    if (this == other)
      return true;

    if (!(other instanceof VOMSGroupAttribute))
      return false;

    if (other == null)
      return false;

    VOMSGroupAttribute that = (VOMSGroupAttribute) other;

    if (getGroup().equals(that.getGroup()))
      return getAttributeDescription().equals(that.getAttributeDescription());

    return false;

  }

  public int hashCode() {

    int result = 14;

    result = 29 * result + getAttributeDescription().hashCode();

    if (getGroup() != null)
      result = 29 * result + getGroup().hashCode();

    return result;

  }

  @Override
  public VOMSAttributeDescription getAttributeDescription() {

    return attributeDescription;
  }

  @Override
  public void setAttributeDescription(VOMSAttributeDescription desc) {

    this.attributeDescription = desc;
  }

  @Override
  public String getValue() {

    return value;
  }

  @Override
  public void setValue(String value) {

    this.value = value;

  }

  @Override
  public void setContext(String context) {

    this.context = context;

  }

  @Override
  public AttributeValue asAttributeValue() {

    AttributeValue val = new AttributeValue(getAttributeDescription().asAttributeClass(),
        getContext(), getValue());
    return val;
  }

  @Override
  public String getName() {

    return attributeDescription.getName();
  }
}
