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
package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.operations.BaseVoReadOperation;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.groups.SearchMembersOperation;
import org.glite.security.voms.admin.persistence.dao.SearchResults;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;

public class SearchUsersOperation extends BaseVoReadOperation<SearchResults> {

  private String searchString;

  private int firstResult;

  private int maxResults;

  private SearchUsersOperation(String searchString, int firstResult, int maxResults) {

    this.searchString = searchString;
    this.firstResult = firstResult;
    this.maxResults = maxResults;

  }

  public SearchResults doExecute() {

    if (CurrentAdmin.instance()
      .hasPermissions(VOMSContext.getVoContext(), SearchMembersOperation.PERSONAL_INFO_READ)) {
      return VOMSUserDAO.instance().search(searchString, firstResult, maxResults);
    } else {

      return VOMSUserDAO.instance().searchBySubject(searchString, firstResult, maxResults);
    }
  }

  public static SearchUsersOperation instance(String searchString, int firstResult,
      int maxResults) {

    return new SearchUsersOperation(searchString, firstResult, maxResults);
  }

  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(),
        VOMSPermission.getContainerReadPermission().setMembershipReadPermission());
  }

}
