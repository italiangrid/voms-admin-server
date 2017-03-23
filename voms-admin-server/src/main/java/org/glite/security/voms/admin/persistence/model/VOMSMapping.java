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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.glite.security.voms.admin.error.IllegalStateException;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NaturalId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "m")
public class VOMSMapping implements Serializable, Comparable<VOMSMapping> {

  private static final Logger log = LoggerFactory.getLogger(VOMSMapping.class);

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "mapping_id")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "userid", nullable = false)
  @ForeignKey(name = "fk_m_usr")
  @NaturalId
  private VOMSUser user;

  @ManyToOne
  @JoinColumn(name = "gid", nullable = false)
  @ForeignKey(name = "fk_m_groups")
  @NaturalId
  private VOMSGroup group;

  @ManyToOne
  @JoinColumn(name = "rid", nullable = true)
  @ForeignKey(name = "fk_m_roles")
  @NaturalId
  private VOMSRole role;

  public VOMSMapping() {

  }

  public VOMSMapping(VOMSUser u, VOMSGroup g, VOMSRole r) {

    user = u;
    group = g;
    role = r;
  }

  public static VOMSMapping instance(VOMSUser u, VOMSGroup g, VOMSRole r) {

    VOMSMapping m = new VOMSMapping(u, g, r);
    return m;

  }

  public static VOMSMapping instance(VOMSUser u, VOMSGroup g) {

    return instance(u, g, null);

  }

  public VOMSGroup getGroup() {

    return group;
  }

  public void setGroup(VOMSGroup group) {

    this.group = group;
  }

  public VOMSRole getRole() {

    return role;
  }

  public void setRole(VOMSRole role) {

    this.role = role;
  }

  public VOMSUser getUser() {

    return user;
  }

  public void setUser(VOMSUser user) {

    this.user = user;
  }

  public Long getId() {

    return id;
  }

  public void setId(Long id) {

    this.id = id;
  }

  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    
    if (this == other) {
      return true;
    }

    if (!(other instanceof VOMSMapping)) {
      return false;
    }
    
    VOMSMapping that = (VOMSMapping) other;

    if (!getUser().equals(that.getUser())) {
      return false;
    }

    if (this.isGroupMapping() && that.isGroupMapping()) {

      boolean result = getGroup().equals(that.getGroup());
      return result;

    }

    if (this.isRoleMapping() && that.isRoleMapping()) {
      boolean result = (getGroup().equals(that.getGroup()) && getRole().equals(
        that.getRole()));
      return result;
    }

    return false;

  }

  public boolean isGroupMapping() {

    if (getUser() == null)
      throw new IllegalStateException(
        "This mapping has not been initialized (user== null)!");

    if (getGroup() == null)
      throw new IllegalStateException(
        "This mapping has not been initialized (group == null)!");

    return (getRole() == null);
  }

  public boolean isRoleMapping() {

    if (getUser() == null)
      throw new IllegalStateException(
        "This mapping has not been initialized (user== null)!");

    if (getGroup() == null)
      throw new IllegalStateException(
        "This mapping has not been initialized (group == null)!");

    return (getRole() != null);
  }

  public int hashCode() {

    int result = 14;

    result = 29 * result + getUser().hashCode();

    result = 29 * result + getGroup().hashCode();

    if (getRole() != null)
      result = 29 * result + getRole().hashCode();

    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {

    return "VOMSMapping [id=" + id + ", user=" + user.getId() + ", group="
      + group.getName() + ", role=" + role + "]";
  }

  public String getFQAN() {

    StringBuffer buf = new StringBuffer();

    if (getGroup() != null)
      buf.append(getGroup());

    if (getRole() != null)
      buf.append("/" + getRole());

    return buf.toString();

  }

  public int compareTo(VOMSMapping that) {

    if (this.equals(that))
      return 0;
    
    if (!getUser().equals(that.getUser())) {
      int result = getUser().compareTo(that.getUser());
      return result;
    }

    if (isGroupMapping() && that.isGroupMapping()) {
      int result = getGroup().compareTo(that.getGroup());
      return result;
    }

    if (isRoleMapping() && that.isRoleMapping()) {

      int result;
      int groupResult = getGroup().compareTo(that.getGroup());

      if (groupResult == 0) {
        result = getRole().compareTo(that.getRole());
      } else {
        result = groupResult;
      }

      return result;
    }

    // One role, one group, will sort against groups
    if (this.isGroupMapping() && that.isRoleMapping()) {
      
      int result;
      int groupResult = getGroup().compareTo(that.getGroup());
      if (groupResult == 0) {
        result = -1;
      } else {
        result = groupResult;
      }
      return result;
    }

    if (this.isRoleMapping() && that.isGroupMapping()) {
      int result;
      int groupResult = getGroup().compareTo(that.getGroup());
      if (groupResult == 0) {
        result = 1;
      } else {
        result = groupResult;
      }
      return result;
    }

    // This should never happen --> throw exception?
    int result = 1;
    return result;
  }

}
