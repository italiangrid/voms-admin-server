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
package org.glite.security.voms.admin.operations;

import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.error.NoSuchGroupException;
import org.glite.security.voms.admin.persistence.error.NoSuchRoleException;
import org.glite.security.voms.admin.persistence.model.ACL;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.util.PathNamingScheme;

public class VOMSContext {

  VOMSGroup group;

  VOMSRole role;

  protected VOMSContext(VOMSGroup g, VOMSRole r) {

    this.group = g;
    this.role = r;

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

  public static VOMSContext instance(VOMSGroup g) {

    return instance(g, null);
  }

  public static VOMSContext instance(VOMSGroup g, VOMSRole r) {

    if (g == null)
      throw new IllegalArgumentException(
        "null group passed as argument to constructor!");

    return new VOMSContext(g, r);
  }

  public static VOMSContext instance(String contextString) {

    if (contextString == null)
      throw new IllegalArgumentException("null is not a valid VOMS context!");

    if (PathNamingScheme.isQualifiedRole(contextString)) {

      VOMSGroup g = VOMSGroupDAO.instance().findByName(
        PathNamingScheme.getGroupName(contextString));
      VOMSRole r = VOMSRoleDAO.instance().findByName(
        PathNamingScheme.getRoleName(contextString));

      if (g == null)
        throw new NoSuchGroupException("Group '"
          + PathNamingScheme.getGroupName(contextString)
          + "' is not defined for this vo.");

      if (r == null)
        throw new NoSuchRoleException("Role '"
          + PathNamingScheme.getRoleName(contextString)
          + "' is not defined for this vo.");

      return new VOMSContext(g, r);
    }
    
    if (PathNamingScheme.isGroup(contextString)) {
      VOMSGroup g = VOMSGroupDAO.instance().findByName(contextString);

      if (g == null)
        throw new NoSuchGroupException("Group '"
          + PathNamingScheme.getGroupName(contextString)
          + "' is not defined for this vo.");

      return new VOMSContext(g, null);

    }

    throw new IllegalArgumentException(
      "incorrect context string passed as argument to constructor!");

  }

  public static VOMSContext instance(Long groupId, Long roleId) {

    VOMSGroup g = VOMSGroupDAO.instance().findById(groupId);

    if (g == null)
      throw new NoSuchGroupException("Group identified by id '" + groupId
        + "' not found!");

    VOMSRole r = null;

    if (roleId != null)
      r = VOMSRoleDAO.instance().findById(roleId);

    return instance(g, r);
  }

  public boolean isGroupContext() {

    return (getRole() == null);
  }

  public ACL getACL() {

    if (isGroupContext())
      return getGroup().getACL();
    else
      return getRole().getACL(getGroup());

  }

  public String toString() {

    StringBuffer buf = new StringBuffer();
    if (group != null)
      buf.append(group.getName());

    if (role != null) {
      buf.append("/" + role);
    }
    return buf.toString();

  }

  public static VOMSContext getVoContext() {

    return instance(VOMSGroupDAO.instance().getVOGroup());

  }

}
