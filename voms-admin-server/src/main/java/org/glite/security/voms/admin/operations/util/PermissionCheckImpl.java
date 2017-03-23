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
package org.glite.security.voms.admin.operations.util;

import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class PermissionCheckImpl implements PermissionCheck {

  final CurrentAdmin admin;
  final VOMSContext context;
  final VOMSPermission permission;

  public static PermissionCheckImpl instance(CurrentAdmin admin,
    VOMSContext context, VOMSPermission permission) {

    return new PermissionCheckImpl(admin, context, permission);
  }

  private PermissionCheckImpl(CurrentAdmin admin, VOMSContext context,
    VOMSPermission permission) {
    this.admin = admin;
    this.context = context;
    this.permission = permission;
  }

  @Override
  public CurrentAdmin getAdmin() {

    return admin;
  }

  @Override
  public VOMSContext getContext() {

    return context;
  }

  @Override
  public VOMSPermission getPermission() {

    return permission;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((admin == null) ? 0 : admin.hashCode());
    result = prime * result + ((context == null) ? 0 : context.hashCode());
    result = prime * result
      + ((permission == null) ? 0 : permission.hashCode());
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
    PermissionCheckImpl other = (PermissionCheckImpl) obj;
    if (admin == null) {
      if (other.admin != null)
        return false;
    } else if (!admin.equals(other.admin))
      return false;
    if (context == null) {
      if (other.context != null)
        return false;
    } else if (!context.equals(other.context))
      return false;
    if (permission == null) {
      if (other.permission != null)
        return false;
    } else if (!permission.equals(other.permission))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "[admin=" + admin + ", context=" + context + ", permission="
        + permission + "]";
  }
}
