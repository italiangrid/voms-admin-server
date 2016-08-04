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
package org.glite.security.voms.admin.view.preparers.acl;

import java.util.List;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparerSupport;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.operations.roles.ListRolesOperation;
import org.glite.security.voms.admin.operations.users.ListUsersOperation;
import org.glite.security.voms.admin.persistence.dao.CertificateDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSCADAO;
import org.glite.security.voms.admin.persistence.model.VOMSCA;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class AddEntryPreparer extends ViewPreparerSupport {

  @Override
  public void execute(TilesRequestContext tilesContext,
    AttributeContext attributeContext) throws PreparerException {

    List<VOMSGroup> groups = (List<VOMSGroup>) ListGroupsOperation.instance()
      .execute();
    List<VOMSRole> roles = (List<VOMSRole>) ListRolesOperation.instance()
      .execute();
    List<VOMSUser> users = (List<VOMSUser>) ListUsersOperation.instance()
      .execute();
    List<VOMSCA> cas = (List<VOMSCA>) VOMSCADAO.instance().getValid();

    tilesContext.getRequestScope().put("voCertificates",
      CertificateDAO.instance().getAll());
    tilesContext.getRequestScope().put("voUsers", users);
    tilesContext.getRequestScope().put("voGroups", groups);
    tilesContext.getRequestScope().put("voRoles", roles);
    tilesContext.getRequestScope().put("voCAs", cas);
  }

}
