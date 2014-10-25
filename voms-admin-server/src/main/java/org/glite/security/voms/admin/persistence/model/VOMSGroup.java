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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.glite.security.voms.admin.persistence.error.NoSuchAttributeException;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VOMSGroup implements Serializable, Comparable<VOMSGroup> {

  public static final Logger log = LoggerFactory.getLogger(VOMSGroup.class);

  private static final long serialVersionUID = -4693441755811017977L;

  public VOMSGroup() {

    must = new Boolean(true);
  }

  @Id
  @Column(name = "gid")
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "VOMS_GROUP_SEQ")
  @SequenceGenerator(name = "VOMS_GROUP_SEQ", sequenceName = "VOMS_GROUP_SEQ")
  Long id;

  @Column(name = "dn", nullable = false, unique = true)
  String name;

  @ManyToOne(optional = true)
  VOMSGroup parent;

  @Column(nullable = false)
  Boolean must;

  @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "group")
  @org.hibernate.annotations.Cascade(
    value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
  Set<VOMSGroupAttribute> attributes = new HashSet<VOMSGroupAttribute>();

  @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "group",
    fetch = FetchType.EAGER)
  @Sort(type = SortType.NATURAL)
  Set<VOMSMapping> mappings = new TreeSet<VOMSMapping>();

  @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "group")
  @org.hibernate.annotations.Cascade(
    value = { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
  Set<ACL> acls = new HashSet<ACL>();

  Set<GroupManager> managers = new HashSet<GroupManager>();

  Boolean restricted;

  String description;

  public String getName() {

    return name;
  }

  public void setName(String name) {

    this.name = name;
  }

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
   * @return Returns the must.
   */
  public Boolean getMust() {

    return must;
  }

  /**
   * @param must
   *          The must to set.
   */
  public void setMust(Boolean must) {

    this.must = must;
  }

  /**
   * @return Returns the parent.
   */
  public VOMSGroup getParent() {

    return parent;
  }

  /**
   * @param parent
   *          The parent to set.
   */
  public void setParent(VOMSGroup parent) {

    this.parent = parent;
  }

  public Set<VOMSGroupAttribute> getAttributes() {

    return attributes;
  }

  public void setAttributes(Set<VOMSGroupAttribute> attributes) {

    this.attributes = attributes;
  }

  public boolean isRootGroup() {

    return this.equals(parent);
  }

  public void addAttribute(VOMSGroupAttribute val) {

    attributes.add(val);
  }

  public VOMSGroupAttribute getAttributeByName(String name) {

    Iterator<VOMSGroupAttribute> i = attributes.iterator();

    while (i.hasNext()) {
      VOMSGroupAttribute tmp = i.next();

      if (tmp.getName().equals(name))
        return tmp;
    }

    return null;

  }

  public void deleteAttribute(VOMSGroupAttribute val) {

    if (!attributes.contains(val))
      throw new NoSuchAttributeException("Attribute \"" + val.getName()
        + "\" undefined for group" + this);

    attributes.remove(val);
  }

  public void deleteAttributeByName(String attrName) {

    deleteAttribute(getAttributeByName(attrName));

  }

  public boolean equals(Object other) {

    if (this == other)
      return true;

    if (!(other instanceof VOMSGroup))
      return false;

    VOMSGroup that = (VOMSGroup) other;

    if (that == null)
      return false;

    return (getName().equals(that.getName()));

  }

  public boolean hasMember(VOMSUser u) {

    VOMSMapping m = new VOMSMapping(u, this, null);
    return mappings.contains(m);

  }

  public Set<VOMSUser> getMembers() {

    SortedSet<VOMSUser> res = new TreeSet<VOMSUser>();

    Iterator<VOMSMapping> mIter = mappings.iterator();
    while (mIter.hasNext()) {
      VOMSMapping m = mIter.next();
      if (m.isGroupMapping())
        res.add(m.getUser());
    }

    return Collections.unmodifiableSortedSet(res);

  }

  public Set<String> getMembersEmailAddresses() {

    SortedSet<String> res = new TreeSet<String>();

    Iterator<VOMSMapping> mIter = mappings.iterator();

    while (mIter.hasNext()) {
      VOMSMapping m = mIter.next();
      if (m.isGroupMapping())
        res.add(m.getUser().getEmailAddress());
    }

    return Collections.unmodifiableSortedSet(res);

  }

  public boolean isDescendant(VOMSGroup g) {

    return name.startsWith(g.getName());
  }

  public Set<VOMSMapping> getMappings() {

    return mappings;
  }

  public void setMappings(Set<VOMSMapping> mappings) {

    this.mappings = mappings;
  }

  public int hashCode() {

    return getName().hashCode();
  }

  public String toString() {

    return getName();
  }

  public int compareTo(VOMSGroup that) {

    if (this.equals(that))
      return 0;

    if (that == null)
      return 1;

    return this.getName().compareTo(that.getName());
  }

  public Set<ACL> getAcls() {

    return acls;
  }

  public void setAcls(Set<ACL> acls) {

    this.acls = acls;
  }

  protected ACL getACL(boolean defaultACL) {

    if (getAcls().isEmpty())
      return null;

    Iterator<ACL> i = getAcls().iterator();
    while (i.hasNext()) {
      ACL a = i.next();
      if (a.getDefaultACL().booleanValue() == defaultACL
        && a.getContext().isGroupContext())
        return a;
    }

    return null;
  }

  public ACL getACL() {

    return getACL(false);
  }

  public ACL getDefaultACL() {

    return getACL(true);
  }

  public void importACL(ACL acl) {

    ACL importedACL = new ACL(this, false);

    importedACL.getPermissions().putAll(acl.getPermissions());

    getAcls().add(importedACL);

  }

  public Boolean getRestricted() {

    return restricted;
  }

  public void setRestricted(Boolean restricted) {

    this.restricted = restricted;
  }

  public String getDescription() {

    return description;
  }

  public void setDescription(String description) {

    this.description = description;
  }

  /**
   * @return the managers
   */
  public Set<GroupManager> getManagers() {

    return managers;
  }

  /**
   * @param managers
   *          the managers to set
   */
  public void setManagers(Set<GroupManager> managers) {

    this.managers = managers;
  }

  public void addMapping(VOMSMapping m) {

    getMappings().add(m);
  }

  public boolean removeMapping(VOMSMapping m) {

    return getMappings().remove(m);
  }

}
