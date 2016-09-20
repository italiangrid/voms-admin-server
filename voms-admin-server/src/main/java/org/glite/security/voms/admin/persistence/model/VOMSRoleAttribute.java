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
package org.glite.security.voms.admin.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.glite.security.voms.admin.operations.VOMSContext;

@Entity
@Table(name = "role_attrs")
public class VOMSRoleAttribute extends VOMSGroupAttribute {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  @Id
  @ManyToOne
  @JoinColumn(name = "r_id")
  VOMSRole role;

  public VOMSRole getRole() {

    return role;
  }

  public void setRole(VOMSRole role) {

    this.role = role;
  }

  public VOMSRoleAttribute() {

  }

  private VOMSRoleAttribute(VOMSAttributeDescription desc, String value,
    VOMSGroup g, VOMSRole r) {

    super(desc, value, g);

    this.role = r;

  }

  public boolean equals(Object other) {

    if (this == other)
      return true;

    if (!(other instanceof VOMSRoleAttribute))
      return false;

    VOMSRoleAttribute that = (VOMSRoleAttribute) other;

    if (super.equals(that)) {

      if (getRole().equals(that.getRole()))
        return true;
    }

    return false;
  }

  public int hashCode() {

    int result = 14;

    result = 29 * result + attributeDescription.hashCode();
    result = 29 * result + group.hashCode();

    return result;
  }

  public static VOMSRoleAttribute instance(VOMSAttributeDescription desc,
    String value, VOMSGroup g, VOMSRole r) {

    return new VOMSRoleAttribute(desc, value, g, r);
  }

  public static VOMSRoleAttribute instance(String attrName, String attrDesc,
    String attrValue, VOMSGroup g, VOMSRole r) {

    VOMSAttributeDescription desc = new VOMSAttributeDescription(attrName,
      attrDesc);
    VOMSRoleAttribute instance = new VOMSRoleAttribute(desc, attrValue, g, r);

    return instance;
  }

  public String getContext() {

    return VOMSContext.instance(getGroup(), getRole()).toString();

  }

}
