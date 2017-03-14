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
package org.glite.security.voms.admin.view.preparers.acl;

import java.util.Collections;
import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.glite.security.voms.admin.error.VOMSAuthorizationException;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.operations.roles.ListRolesOperation;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;

public class ManagePreparer extends ViewPreparerSupport {

  @Override
  public void execute(TilesRequestContext tilesContext,
    AttributeContext attributeContext) throws PreparerException {

    List<VOMSGroup> groups = Collections.EMPTY_LIST;
    List<VOMSRole> roles = Collections.EMPTY_LIST;

    try {
      groups = (List<VOMSGroup>) ListGroupsOperation.instance().execute();

      roles = (List<VOMSRole>) ListRolesOperation.instance().execute();

    } catch (VOMSAuthorizationException e) {

      // swallow authorization exception
    }

    tilesContext.getRequestScope().put("voGroups", groups);
    tilesContext.getRequestScope().put("voRoles", roles);

  }
}
