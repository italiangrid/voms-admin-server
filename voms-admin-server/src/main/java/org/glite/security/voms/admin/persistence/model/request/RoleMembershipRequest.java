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
package org.glite.security.voms.admin.persistence.model.request;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "role_membership_req")
public class RoleMembershipRequest extends Request {

  /**
     * 
     */
  private static final long serialVersionUID = 1L;

  String groupName;
  String roleName;

  public RoleMembershipRequest() {

  }

  public String getGroupName() {

    return groupName;
  }

  public void setGroupName(String groupName) {

    this.groupName = groupName;
  }

  /**
   * @return the roleName
   */
  public String getRoleName() {

    return roleName;
  }

  /**
   * @param roleName
   *          the roleName to set
   */
  public void setRoleName(String roleName) {

    this.roleName = roleName;
  }

  public String getTypeName() {

    return "Role membership request";
  }

  public String getFQAN() {

    return getGroupName() + "/Role=" + getRoleName();
  }

  @Override
  public boolean equals(Object other) {

    if (this == other)
      return true;

    if (!(other instanceof RoleMembershipRequest))
      return false;

    if (other == null)
      return false;

    RoleMembershipRequest that = (RoleMembershipRequest) other;
    EqualsBuilder builder = new EqualsBuilder();

    return builder.appendSuper(super.equals(other))
      .append(groupName, that.groupName).append(roleName, that.roleName)
      .isEquals();
  }

  @Override
  public int hashCode() {

    HashCodeBuilder builder = new HashCodeBuilder(17, 43);
    builder.appendSuper(super.hashCode()).append(groupName).append(roleName);
    return builder.toHashCode();
  }

  @Override
  public String toString() {

    ToStringBuilder builder = new ToStringBuilder(this);
    builder.appendSuper(super.toString()).append("groupName", groupName)
      .append("roleName", roleName);
    return builder.toString();
  }
}
