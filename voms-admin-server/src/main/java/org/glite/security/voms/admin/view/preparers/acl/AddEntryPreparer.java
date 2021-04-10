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

import java.util.List;
import java.util.Map;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.request.Request;
import org.glite.security.voms.admin.operations.groups.ListGroupsOperation;
import org.glite.security.voms.admin.operations.roles.ListRolesOperation;
import org.glite.security.voms.admin.operations.users.ListUsersOperation;
import org.glite.security.voms.admin.persistence.dao.CertificateDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSCADAO;
import org.glite.security.voms.admin.persistence.model.VOMSCA;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class AddEntryPreparer implements ViewPreparer {

  @SuppressWarnings("unchecked")
  @Override
  public void execute(Request request, AttributeContext attributeContext) {

    List<VOMSGroup> groups = (List<VOMSGroup>) ListGroupsOperation.instance().execute();
    List<VOMSRole> roles = (List<VOMSRole>) ListRolesOperation.instance().execute();
    List<VOMSUser> users = (List<VOMSUser>) ListUsersOperation.instance().execute();
    List<VOMSCA> cas = (List<VOMSCA>) VOMSCADAO.instance().getValid();

    Map<String, Object> context = request.getContext(Request.REQUEST_SCOPE);
    context.put("voCertificates", CertificateDAO.instance().getAll());
    context.put("voUsers", users);
    context.put("voGroups", groups);
    context.put("voRoles", roles);
    context.put("voCAs", cas);
  }

}
