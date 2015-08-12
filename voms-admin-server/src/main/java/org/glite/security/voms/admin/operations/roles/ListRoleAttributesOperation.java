/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package org.glite.security.voms.admin.operations.roles;

import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;

public class ListRoleAttributesOperation extends BaseVomsOperation {

  protected VOMSGroup _vomsGroup;
  protected VOMSRole _vomsRole;

  private ListRoleAttributesOperation(VOMSGroup g, VOMSRole r) {

    this._vomsGroup = g;
    this._vomsRole = r;
  }

  protected Object doExecute() {

    return _vomsRole.getAttributesInGroup(_vomsGroup);

  }

  protected void setupPermissions() {

    addRequiredPermissionOnPath(_vomsGroup.getParent(),
      VOMSPermission.getContainerReadPermission());

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getEmptyPermissions().setAttributesReadPermission());

    addRequiredPermission(VOMSContext.instance(_vomsGroup, _vomsRole),
      VOMSPermission.getEmptyPermissions().setAttributesReadPermission());

  }

  public static ListRoleAttributesOperation instance(VOMSGroup g, VOMSRole r) {

    return new ListRoleAttributesOperation(g, r);
  }

}
