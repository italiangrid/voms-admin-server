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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.glite.security.voms.admin.persistence.error.NoSuchAttributeException;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSRoleAttribute;
import org.hibernate.annotations.SortNatural;

@Entity
@Table(name="roles")
public class VOMSRole implements Serializable, Comparable<VOMSRole> {

  private static final long serialVersionUID = -5063337678658382573L;

  public VOMSRole() {

  }

  public VOMSRole(String name) {

    this.name = name;
  }

  @Id
  @Column(name = "rid")
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "VOMS_ROLE_SEQ")
  @SequenceGenerator(name = "VOMS_ROLE_SEQ", sequenceName = "VOMS_ROLE_SEQ")
  Long id;

  @Column(name = "role", nullable = false, unique = true)
  String name;

  @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "role", orphanRemoval=true)
  Set<VOMSRoleAttribute> attributes = new HashSet<VOMSRoleAttribute>();

  @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "role",
    fetch = FetchType.EAGER)
  @SortNatural
  Set<VOMSMapping> mappings = new TreeSet<VOMSMapping>();

  @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "role", orphanRemoval=true)
  Set<ACL> acls = new HashSet<ACL>();

  /**
   * @return Returns the id.
   */
  public Long getId() {

    return id;
  }

  /**
   * @param id
   *          The id to set.
   */
  public void setId(Long id) {

    this.id = id;
  }

  /**
   * @return Returns the name.
   */
  public String getName() {

    return name;
  }

  /**
   * @param name
   *          The name to set.
   */
  public void setName(String name) {

    this.name = name;
  }

  public boolean equals(Object other) {

    if (this == other)
      return true;

    if (!(other instanceof VOMSRole))
      return false;

    VOMSRole that = (VOMSRole) other;

    if (that == null)
      return false;

    return getName().equals(that.getName());
  }

  public int hashCode() {

    return getName().hashCode();
  }

  public int compareTo(VOMSRole that) {

    if (this.equals(that))
      return 0;

    if ((that.name == null) && (this.name == null))
      return 1;

    if ((that.name == null) || (this.name == null))
      return -1;

    return this.name.compareTo(that.name);
  }

  public String toString() {

    return "Role=" + name;
  }

  public Set<VOMSMapping> getMappings() {

    return mappings;
  }

  public void setMappings(Set<VOMSMapping> mappings) {

    this.mappings = mappings;
  }

  public Set<VOMSUser> getUsers(VOMSGroup g) {

    SortedSet<VOMSUser> res = new TreeSet<VOMSUser>();
    Iterator<VOMSMapping> mIter = mappings.iterator();

    while (mIter.hasNext()) {

      VOMSMapping m = mIter.next();
      if (m.getGroup().equals(g))
        res.add(m.getUser());
    }

    return Collections.unmodifiableSortedSet(res);
  }

  public Set<String> getMembersEmailAddresses(VOMSGroup g) {

    SortedSet<String> res = new TreeSet<String>();
    Iterator<VOMSMapping> mIter = mappings.iterator();

    while (mIter.hasNext()) {

      VOMSMapping m = mIter.next();
      if (m.getGroup().equals(g))
        res.add(m.getUser().getEmailAddress());
    }

    return Collections.unmodifiableSortedSet(res);
  }

  public boolean isAssignedTo(VOMSGroup g, VOMSUser u) {

    if (g == null)
      throw new IllegalArgumentException("g must not be null!");

    if (u == null)
      throw new IllegalArgumentException("u must not be null!");

    VOMSMapping m = new VOMSMapping(u, g, this);
    return mappings.contains(m);
  }

  public VOMSRoleAttribute getAttributeByName(VOMSGroup g, String attrName) {

    Iterator<VOMSRoleAttribute> i = attributes.iterator();
    while (i.hasNext()) {
      VOMSRoleAttribute rav = i.next();
      if (rav.getGroup().equals(g) && rav.getName().equals(attrName))
        return rav;
    }

    return null;
  }

  public Set<VOMSRoleAttribute> getAttributesInGroup(VOMSGroup g) {

    Set<VOMSRoleAttribute> result = new HashSet<VOMSRoleAttribute>();
    Iterator<VOMSRoleAttribute> i = attributes.iterator();

    while (i.hasNext()) {
      VOMSRoleAttribute rav = i.next();
      if (rav.getGroup().equals(g))
        result.add(rav);
    }

    return result;

  }

  public void addAttribute(VOMSRoleAttribute val) {

    attributes.add(val);

  }

  public void deleteAttribute(VOMSRoleAttribute val) {

    if (!attributes.contains(val))
      throw new NoSuchAttributeException("Attribute \"" + val.getName()
        + "\" not defined for \"" + this + "\" in group \"" + val.getGroup()
        + "\".");

    attributes.remove(val);

  }

  public void addMapping(VOMSMapping m) {

    getMappings().add(m);
  }

  public boolean removeMapping(VOMSMapping m) {

    return getMappings().remove(m);
  }

  public Set<VOMSRoleAttribute> getAttributes() {

    return attributes;
  }

  public void setAttributes(Set<VOMSRoleAttribute> attributes) {

    this.attributes = attributes;
  }

  public Set<ACL> getAcls() {

    return acls;
  }

  public void setAcls(Set<ACL> acls) {

    this.acls = acls;
  }

  public ACL getACL(VOMSGroup g) {

    ACL result = null;

    Iterator<ACL> i = getAcls().iterator();

    while (i.hasNext()) {

      ACL tmp = i.next();
      if (tmp.getGroup().equals(g) && (!tmp.getContext().isGroupContext())) {

        result = tmp;
        break;
      }
    }

    return result;

  }

  public void importACL(VOMSGroup g) {

    ACL groupACL;

    if (g.getDefaultACL() == null)
      groupACL = g.getACL();
    else
      groupACL = g.getDefaultACL();

    ACL newACL = new ACL(g, this, false);

    newACL.getPermissions().putAll(groupACL.getPermissions());

    getAcls().add(newACL);

  }

}
