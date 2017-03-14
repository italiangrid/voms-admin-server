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
package org.glite.security.voms.admin.operations.groups;

import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;

public class ListAttributesForGroupOperation extends BaseVomsOperation {

  VOMSGroup _vomsGroup;

  private ListAttributesForGroupOperation(VOMSGroup g) {

    _vomsGroup = g;

  }

  protected Object doExecute() {

    return _vomsGroup.getAttributes();
  }

  protected void setupPermissions() {

    addRequiredPermissionOnPath(_vomsGroup.getParent(),
      VOMSPermission.getContainerReadPermission());

    addRequiredPermission(VOMSContext.instance(_vomsGroup), VOMSPermission
      .getEmptyPermissions().setAttributesReadPermission());

  }

  public static ListAttributesForGroupOperation instance(VOMSGroup g) {

    return new ListAttributesForGroupOperation(g);
  }
}
