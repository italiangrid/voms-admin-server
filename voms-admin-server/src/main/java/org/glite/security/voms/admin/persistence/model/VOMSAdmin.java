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

import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.glite.security.voms.admin.error.VOMSSyntaxException;
import org.glite.security.voms.admin.util.PathNamingScheme;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "admins")
public class VOMSAdmin implements Serializable, Cloneable {

  private static final long serialVersionUID = -5459874418491929253L;

  @Id
  @Column(name = "adminid")
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false)
  @NaturalId
  String dn;

  @ManyToOne(targetEntity = VOMSCA.class, optional = false)
  @JoinColumn(name = "ca", nullable = false)
  VOMSCA ca;

  @Column(name = "email_address", nullable = true)
  String emailAddress;

  public VOMSAdmin() {

  }

  /**
   * @return Returns the ca.
   */
  public VOMSCA getCa() {

    return ca;
  }

  /**
   * @param ca
   *          The ca to set.
   */
  public void setCa(VOMSCA ca) {

    this.ca = ca;
  }

  /**
   * @return Returns the dn.
   */
  public String getDn() {

    return dn;
  }

  /**
   * @param dn
   *          The dn to set.
   */
  public void setDn(String dn) {

    this.dn = dn;
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

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((ca == null) ? 0 : ca.hashCode());
    result = prime * result + ((dn == null) ? 0 : dn.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {

    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    VOMSAdmin other = (VOMSAdmin) obj;
    if (ca == null) {
      if (other.ca != null)
        return false;
    } else if (!ca.equals(other.ca))
      return false;
    if (dn == null) {
      if (other.dn != null)
        return false;
    } else if (!dn.equals(other.dn))
      return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#clone()
   */
  public Object clone() throws CloneNotSupportedException {

    VOMSAdmin newInstance = (VOMSAdmin) super.clone();
    newInstance.id = id;
    newInstance.dn = dn;
    newInstance.ca = ca;
    return newInstance;
  }

  

  @Override
  public String toString() {

    return "[dn=" + dn + ", ca=" + ca + ", emailAddress="
      + emailAddress + "]";
  }

  public String getEmailAddress() {

    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {

    this.emailAddress = emailAddress;
  }

  public boolean isInternalAdmin() {

    return getCa().getSubjectString().startsWith(
      VOMSServiceConstants.INTERNAL_DN_PREFIX);
  }

  public boolean isUnauthenticated() {

    return (getDn().equals(VOMSServiceConstants.UNAUTHENTICATED_CLIENT) && getCa()
      .getSubjectString().equals(VOMSServiceConstants.VIRTUAL_CA));
  }

  public boolean isGroupAdmin() {

    boolean result;
    try {

      result = PathNamingScheme.isGroup(getDn());

    } catch (VOMSSyntaxException e) {

      return false;
    }

    return result;
  }

  public boolean isRoleAdmin() {

    boolean result;

    try {

      result = PathNamingScheme.isQualifiedRole(getDn());

    } catch (VOMSSyntaxException e) {

      return false;
    }
    return result;
  }

}
