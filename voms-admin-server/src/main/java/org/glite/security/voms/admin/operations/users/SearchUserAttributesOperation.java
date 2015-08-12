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
package org.glite.security.voms.admin.operations.users;

import org.glite.security.voms.admin.operations.BaseVomsOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.persistence.dao.VOMSAttributeDAO;

public class SearchUserAttributesOperation extends BaseVomsOperation {

  private String searchString;

  private int firstResult;

  private int maxResults;

  private SearchUserAttributesOperation(String sString, int firstRes, int maxRes) {

    this.searchString = sString;
    this.firstResult = firstRes;
    this.maxResults = maxRes;
  }

  protected Object doExecute() {

    return VOMSAttributeDAO.instance().searchUserAttributes(searchString,
      firstResult, maxResults);
  }

  protected void setupPermissions() {

    addRequiredPermission(VOMSContext.getVoContext(), VOMSPermission
      .getEmptyPermissions().setAttributesReadPermission());

  }

  public static SearchUserAttributesOperation instance(String searchString,
    int firstResult, int maxResults) {

    return new SearchUserAttributesOperation(searchString, firstResult,
      maxResults);
  }

}
