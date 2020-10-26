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
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.glite.security.voms.admin.util.DNUtil;

@Entity
@Table(name = "ca")
public class VOMSCA implements Serializable {

  /**
     * 
     */
  private static final long serialVersionUID = -2633375466574044765L;

  @Id
  @Column(name = "cid")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  Short id;

  @Column(name = "subject_string", unique = true, nullable = false)
  String subjectString;

  String description;

  @Column(name = "creation_time", nullable = false)
  Date creationTime;

  public VOMSCA() {

    // TODO Auto-generated constructor stub
  }

  public VOMSCA(X509Certificate cert, String description) {

    assert cert != null : "X509Certificate is null!";

    subjectString = DNUtil.getOpenSSLSubject(cert.getSubjectX500Principal());
    creationTime = new Date();

    this.description = description;
  }

  public VOMSCA(String name, String desc) {

    this.subjectString = name;
    this.description = desc;
    creationTime = new Date();

  }

  /**
   * @return Returns the description.
   */
  public String getDescription() {

    return description;
  }

  /**
   * @param description
   *          The description to set.
   */
  public void setDescription(String description) {

    this.description = description;
  }

  /**
   * @return Returns the dn.
   */
  public String getSubjectString() {

    return subjectString;
  }

  /**
   * @param dn
   *          The dn to set.
   */
  public void setSubjectString(String dn) {

    this.subjectString = dn;
  }

  /**
   * @return Returns the id.
   */
  public Short getId() {

    return id;
  }

  /**
   * @param id
   *          The id to set.
   */
  public void setId(Short id) {

    this.id = id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {

    return subjectString;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((subjectString == null) ? 0 : subjectString.hashCode());
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
    VOMSCA other = (VOMSCA) obj;
    if (subjectString == null) {
      if (other.subjectString != null)
        return false;
    } else if (!subjectString.equals(other.subjectString))
      return false;
    return true;
  }

  public String getShortName() {

    return subjectString;
  }

  @Deprecated
  public String getDn() {

    return subjectString;
  }

  @Deprecated
  public void setDn(String dn) {

    this.subjectString = dn;
  }

  public Date getCreationTime() {

    return creationTime;
  }

  public void setCreationTime(Date creationTime) {

    this.creationTime = creationTime;
  }

}
